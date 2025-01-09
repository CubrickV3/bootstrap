package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminControllerImp {

    private final RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminControllerImp(UserRepository userRepository,
                              PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String usersListPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/new_user")
    public String newUserPage(Model model) {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_USER"));
        roles.add(new Role("ROLE_ADMIN"));
        model.addAttribute("user", user);
        model.addAttribute("role", roles);
        return "admin/new_user";
    }

    @PostMapping()
    public String createUser(@ModelAttribute User user, @RequestParam(value = "role") Set<Role> roles) {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else {
            return "redirect:/admin/new_user";
        }
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        Set<Role> roles = user.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("role", roles);
        return "admin/edit_user";
    }

    @PatchMapping("/{id}")
    public String editUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "role") Set<Role> roles,
                           @PathVariable Long id) {
        User user1 = userRepository.findById(id).get();
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setRoles(roles);
        userRepository.save(user1);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        user.setRoles(null);
        userRepository.delete(user);
        return "redirect:/admin";
    }

}

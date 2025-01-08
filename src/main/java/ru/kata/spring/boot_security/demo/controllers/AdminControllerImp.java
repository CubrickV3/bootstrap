package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminControllerImp implements AdminController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminControllerImp(UserRepository userRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    @GetMapping
    public String usersListPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }
    @Override
    @GetMapping("/new_user")
    public String newUserPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "admin/new_user";
    }
    @Override
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
    @Override
    @GetMapping("/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "admin/edit_user";
    }
    @Override
    @PatchMapping("/{id}")
    public String editUser(@ModelAttribute("user") User user, @PathVariable Long id) {
        User user1 = userRepository.findById(id).get();
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setRoles(user.getRoles());
        userRepository.save(user1);
        return "redirect:/admin";
    }
    @Override
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        userRepository.delete(user);
        return "redirect:/admin";
    }

}
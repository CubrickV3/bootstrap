package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String usersListPage(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "admin/users";
    }

    @GetMapping("/new_user")
    public String newUserPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "admin/new_user";
    }

    @PostMapping()
    public String createUser(@ModelAttribute User user, @RequestParam(value = "role") Set<Role> roles) {
        userService.saveUser(userService.createUser(user, roles));
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "admin/edit_user";
    }

    @PatchMapping("/{id}")
    public String editUser(@ModelAttribute("user") User user, @PathVariable Long id) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}

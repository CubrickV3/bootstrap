package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Set;

public interface AdminController {

    public String usersListPage(Model model);

    public String newUserPage(Model model);

    public String createUser(User user, Set<Role> roles);

    public String editUser(@PathVariable Long id, Model model);

    public String editUser(@ModelAttribute("user") User user, @PathVariable Long id);

    public String deleteUser(Long id);
}

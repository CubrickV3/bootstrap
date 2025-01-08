package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.ui.Model;

import java.security.Principal;

public interface UserController {

    public String userProfile(Principal principal, Model model);
}

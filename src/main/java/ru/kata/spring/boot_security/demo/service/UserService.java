package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Collection;

public interface UserService {

    User findByUsername(String username);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles);
}

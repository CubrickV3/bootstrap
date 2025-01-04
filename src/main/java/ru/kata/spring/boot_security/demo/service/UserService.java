package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder PasswordEncoder;

    @Autowired
    @Lazy
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder PasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.PasswordEncoder = PasswordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user.getRoles()));
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
    @Transactional
    public User createUser(User user, Set<Role> roles) {
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(PasswordEncoder.encode(user.getPassword()));
        Set<Role> roleSet = new HashSet<>();
        for (Role role : roles) {
            Role newRole = role;
            if (newRole != null) {
                roleSet.add(newRole);
            }
        }
        user.setRoles(roleSet);
        return user;
    }

    @Transactional
    public void updateUser(long id, User user) {
        User u = em.find(User.class, id);
        u.setUsername(user.getUsername());
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());
        em.merge(u);
    }

    @Transactional
    public boolean saveUser(User user) {
        User savedUser = userRepository.findByUsername(user.getUsername());
        if (savedUser != null) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class).setParameter("paramId", idMin).getResultList();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }
}

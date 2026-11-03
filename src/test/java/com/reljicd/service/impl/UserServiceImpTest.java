package com.reljicd.service.impl;

import com.reljicd.model.Role;
import com.reljicd.model.User;
import com.reljicd.repository.RoleRepository;
import com.reljicd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImpTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImp userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImp(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void findByUsernameDelegatesToRepository() {
        User expected = new User();
        when(userRepository.findByUsername("shopper")).thenReturn(Optional.of(expected));

        assertSame(expected, userService.findByUsername("shopper").orElseThrow());
        verify(userRepository).findByUsername("shopper");
    }

    @Test
    void findByEmailDelegatesToRepository() {
        User expected = new User();
        when(userRepository.findByEmail("shopper@example.com")).thenReturn(Optional.of(expected));

        assertSame(expected, userService.findByEmail("shopper@example.com").orElseThrow());
        verify(userRepository).findByEmail("shopper@example.com");
    }

    @Test
    void saveUserEncodesPasswordActivatesUserAndAssignsUserRole() {
        User user = new User();
        user.setPassword("plain-password");
        Role role = new Role();
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        when(roleRepository.findByRole("ROLE_USER")).thenReturn(role);
        when(userRepository.saveAndFlush(user)).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertSame(user, savedUser);
        assertEquals("encoded-password", user.getPassword());
        assertEquals(1, user.getActive());
        assertEquals(1, user.getRoles().size());
        assertSame(role, user.getRoles().iterator().next());
        verify(userRepository).saveAndFlush(user);
    }
}

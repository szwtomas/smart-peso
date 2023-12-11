package com.smartpeso.config;

import com.smartpeso.model.User;
import com.smartpeso.repositories.UserRepository;
import com.smartpeso.services.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserDetailsServiceTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private JwtService jwtServiceMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void userDetailsService_givenUserExists_shouldReturnUser() {
        int userId = 123;
        String email = "john.doe@mail.com";
        User user = new User(userId, email, "password", "user", "John", "Doe");

        SecurityConfig securityConfig = new SecurityConfig(userRepositoryMock, jwtServiceMock);
        UserDetailsService unit = securityConfig.userDetailsService();

        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = unit.loadUserByUsername(email);
        String actualEmail = userDetails.getUsername();

        assertEquals("john.doe@mail.com", actualEmail);
    }

    @Test
    public void userDetailsService_givenUserDoesNotExist_shouldThrowUsernameNotFoundException() {
        when(userRepositoryMock.findByEmail("john.doe@mail.com")).thenReturn(Optional.empty());
        SecurityConfig securityConfig = new SecurityConfig(userRepositoryMock, jwtServiceMock);
        UserDetailsService unit = securityConfig.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> unit.loadUserByUsername("john.doe@mail.com"));
    }
}

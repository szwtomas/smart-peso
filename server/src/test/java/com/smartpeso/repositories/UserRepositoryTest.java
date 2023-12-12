package com.smartpeso.repositories;

import com.smartpeso.model.User;
import com.smartpeso.repositories.exceptions.UserAlreadyExistsException;
import com.smartpeso.repositories.exceptions.UserCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplateMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUser_userDoesNotExist_shouldNotThrowException() {
        String newUserEmail = "john.doe@mail.com";
        String newUserFirstName = "John";
        String newUserLastName = "Doe";
        String newUserRole = "user";
        String newUserPassword = "some-password";
        String salt = "salt";

        when(jdbcTemplateMock.query(anyString(), any(UserRowMapper.class), eq(newUserEmail))).thenReturn(new ArrayList<>());
        when(jdbcTemplateMock.update(anyString(), eq(newUserEmail), eq(newUserPassword), eq(salt), eq(newUserRole), eq(newUserFirstName), eq(newUserLastName))).thenReturn(1);

        UserRepository unit = new UserRepository(jdbcTemplateMock);

        unit.createUser(newUserEmail, newUserPassword, salt, newUserRole, newUserFirstName, newUserLastName);
    }

    @Test
    public void createUser_userDoesNotExistButInsertionFails_shouldThrowUserCreationException() {
        String newUserEmail = "john.doe@mail.com";
        String newUserFirstName = "John";
        String newUserLastName = "Doe";
        String newUserRole = "user";
        String newUserPassword = "some-password";
        String salt = "salt";

        when(jdbcTemplateMock.query(anyString(), any(UserRowMapper.class), eq(newUserEmail))).thenReturn(new ArrayList<>());
        when(jdbcTemplateMock.update(anyString(), eq(newUserEmail), eq(newUserPassword), eq(newUserRole), eq(newUserFirstName), eq(newUserLastName))).thenReturn(0);

        UserRepository unit = new UserRepository(jdbcTemplateMock);

        assertThrows(UserCreationException.class, () -> unit.createUser(newUserEmail, newUserPassword, salt, newUserRole, newUserFirstName, newUserLastName));
    }

    @Test
    public void createUser_userAlreadyExists_shouldThrowUserAlreadyExistsException() {
        int userId = 123;
        String newUserEmail = "john.doe@mail.com";
        String newUserFirstName = "John";
        String newUserLastName = "Doe";
        String newUserRole = "user";
        String newUserPassword = "some-password";
        String password = "password";
        String salt = "salt";
        String role = "user";

        User user = new User(userId, newUserEmail, password, salt, role, newUserFirstName, newUserLastName);

        when(jdbcTemplateMock.query(anyString(), any(UserRowMapper.class), eq(newUserEmail))).thenReturn(List.of(user));

        UserRepository unit = new UserRepository(jdbcTemplateMock);

        assertThrows(UserAlreadyExistsException.class, () -> unit.createUser(newUserEmail, newUserPassword, salt, newUserRole, newUserFirstName, newUserLastName));
    }

    @Test
    public void findByEmail_givenUserDoesNotExist_shouldReturnEmptyOptional() {
        String email = "john.doe@mail.com";
        UserRepository unit = new UserRepository(jdbcTemplateMock);

        when(jdbcTemplateMock.query(anyString(), any(UserRowMapper.class), eq(email))).thenReturn(new ArrayList<>());

        Optional<User> actual = unit.findByEmail(email);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void findByEmail_givenUserExists_shouldReturnOptionalWithExistingUser() {
        int userId = 123;
        String email = "john.doe@mail.com";
        User user = new User(userId, email, "some-password","salt", "user", "John", "Doe");
        UserRepository unit = new UserRepository(jdbcTemplateMock);

        when(jdbcTemplateMock.query(anyString(), any(UserRowMapper.class), eq(email))).thenReturn(List.of(user));

        Optional<User> actual = unit.findByEmail(email);

        assertTrue(actual.isPresent());
        assertEquals(123, actual.get().getUserId());
        assertEquals("john.doe@mail.com", actual.get().getEmail());
        assertEquals("John", actual.get().getFirstName());
        assertEquals("Doe", actual.get().getLastName());
        assertEquals("user", actual.get().getRole());
        assertEquals("salt", actual.get().getSalt());
    }
}

package com.smartpeso.repositories;

import com.smartpeso.model.User;
import com.smartpeso.repositories.exceptions.UserAlreadyExistsException;
import com.smartpeso.repositories.exceptions.UserCreationException;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserRepositoryTest {
    @Mock
    private MongoTemplate mongoMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUser_userDoesNotExist_shouldReturnCreatedUser() {
        String newUserEmail = "john.doe@mail.com";
        String newUserFirstName = "John";
        String newUserLastName = "Doe";
        String newUserRole = "user";
        String newUserPassword = "some-password";

        UserRepository unit = new UserRepository(mongoMock);

        when(mongoMock.findOne(any(Query.class), eq(User.class), eq("users"))).thenReturn(null);
        when(mongoMock.insert(any(), eq("users"))).thenReturn(null);
        User actual = unit.createUser(newUserEmail, newUserPassword, newUserRole, newUserFirstName, newUserLastName);

        assertEquals("john.doe@mail.com", actual.getEmail());
        assertEquals("user", actual.getRole());
        assertEquals("John", actual.getFirstName());
        assertEquals("Doe", actual.getLastName());
    }

    @Test
    public void createUser_userAlreadyExists_shouldThrowUserAlreadyExistsException() {
        String newUserEmail = "john.doe@mail.com";
        String newUserFirstName = "John";
        String newUserLastName = "Doe";
        String newUserPassword = "some-password";
        String newUserRole = "user";

        User existingUser = new User(newUserPassword, newUserEmail, newUserRole, newUserFirstName, newUserLastName);

        UserRepository unit = new UserRepository(mongoMock);
        when(mongoMock.findOne(any(Query.class), eq(User.class), eq("users"))).thenReturn(existingUser);

        assertThrows(UserAlreadyExistsException.class, () -> {
            unit.createUser(newUserEmail, newUserPassword, newUserRole, newUserFirstName, newUserLastName);
        });
    }

    @Test
    public void createUser_insertionFails_shouldThrowUserCreationException() {
        String newUserEmail = "john.doe@mail.com";
        String newUserFirstName = "John";
        String newUserLastName = "Doe";
        String newUserPassword = "some-password";
        String newUserRole = "role";

        UserRepository unit = new UserRepository(mongoMock);

        when(mongoMock.findOne(any(Query.class), eq(User.class), eq("users"))).thenReturn(null);
        when(mongoMock.insert(any(Document.class), anyString())).thenThrow(new RuntimeException("some error"));

        assertThrows(UserCreationException.class, () -> {
            unit.createUser(newUserEmail, newUserPassword, newUserRole, newUserFirstName, newUserLastName);
        });
    }

    @Test
    public void findByEmail_givenUserDoesNotExist_shouldReturnEmptyOptional() {
        UserRepository unit = new UserRepository(mongoMock);

        when(mongoMock.findOne(any(Query.class), eq(User.class), eq("users"))).thenReturn(null);

        Optional<User> actual = unit.findByEmail("john.doe@mail.com");

        assertTrue(actual.isEmpty());
    }

    @Test
    public void findByEmail_givenUserExists_shouldReturnExistingUser() {
        User user = new User("john.doe@mail.com", "some-password", "user", "John", "Doe");
        UserRepository unit = new UserRepository(mongoMock);

        when(mongoMock.findOne(any(Query.class), eq(User.class), eq("users"))).thenReturn(user);

        Optional<User> actual = unit.findByEmail("john.doe@mail.com");

        assertTrue(actual.isPresent());
        assertEquals("john.doe@mail.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("user", user.getRole());
    }
}

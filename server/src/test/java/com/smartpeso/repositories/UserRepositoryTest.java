package com.smartpeso.repositories;

import com.smartpeso.model.User;
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

        UserRepository unit = new UserRepository(mongoMock);

        when(mongoMock.findOne(any(Query.class), eq(User.class), eq("users"))).thenReturn(null);
        when(mongoMock.insert(any(), eq("users"))).thenReturn(null);
        User actual = unit.createUser(newUserEmail, newUserFirstName, newUserLastName);

        assertEquals("john.doe@mail.com", actual.email());
        assertEquals("John", actual.firstName());
        assertEquals("Doe", actual.lastName());
    }

    @Test
    public void createUser_userAlreadyExists_shouldThrowUserAlreadyExistsException() {
        String newUserEmail = "john.doe@mail.com";
        String newUserFirstName = "John";
        String newUserLastName = "Doe";
        User existingUser = new User("someId", newUserEmail, newUserFirstName, newUserLastName);

        UserRepository unit = new UserRepository(mongoMock);
        when(mongoMock.findOne(any(Query.class), eq(User.class), eq("users"))).thenReturn(existingUser);

        assertThrows(UserAlreadyExistsException.class, () -> {
            unit.createUser(newUserEmail, newUserFirstName, newUserLastName);
        });
    }

    @Test
    public void createUser_insertionFails_shouldThrowUserCreationException() {
        String newUserEmail = "john.doe@mail.com";
        String newUserFirstName = "John";
        String newUserLastName = "Doe";

        UserRepository unit = new UserRepository(mongoMock);

        when(mongoMock.findOne(any(Query.class), eq(User.class), eq("users"))).thenReturn(null);
        when(mongoMock.insert(any(Document.class), anyString())).thenThrow(new RuntimeException("some error"));

        assertThrows(UserCreationException.class, () -> {
            unit.createUser(newUserEmail, newUserFirstName, newUserLastName);
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
    public void findByEmail_givenUserDoesNotExist_shouldReturnExpectedUser() {
        User user = new User("SomeId", "john.doe@mail.com", "John", "Doe");
        UserRepository unit = new UserRepository(mongoMock);

        when(mongoMock.findOne(any(Query.class), eq(User.class), eq("users"))).thenReturn(user);

        Optional<User> actual = unit.findByEmail("john.doe@mail.com");

        assertTrue(actual.isPresent());
        assertEquals("john.doe@mail.com", user.email());
        assertEquals("John", user.firstName());
        assertEquals("Doe", user.lastName());
    }
}

package com.smartpeso.services;

import com.smartpeso.model.User;
import com.smartpeso.repositories.UserAlreadyExistsException;
import com.smartpeso.repositories.UserCreationException;
import com.smartpeso.repositories.UserRepository;
import com.smartpeso.services.auth.AuthService;
import com.smartpeso.services.auth.UserCreationResult;
import com.smartpeso.validators.UserValidationException;
import com.smartpeso.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class AuthServiceTest {
    @Mock
    private UserValidator userValidatorMock;

    @Mock
    private UserRepository userRepositoryMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void signUp_userCreatedSuccessfully_shouldReturnSuccessResult() {
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";
        User newUser = new User("someId", email, firstName, lastName);

        when(userRepositoryMock.createUser(eq(email), eq(firstName), eq(lastName))).thenReturn(newUser);
        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock);

        UserCreationResult creationResult = unit.signUp(email, firstName, lastName);
        User createdUser = creationResult.getUser();

        assertTrue(creationResult.isSuccess());
        assertEquals("john.doe@mail.com", createdUser.email());
        assertEquals("John", createdUser.firstName());
        assertEquals("Doe", createdUser.lastName());
    }

    @Test
    public void signUp_userAlreadyExists_shouldReturnUserAlreadyExistsResult() {
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";

        when(userRepositoryMock.createUser(eq(email), eq(firstName), eq(lastName))).thenThrow(new UserAlreadyExistsException("some error"));
        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock);

        UserCreationResult creationResult = unit.signUp(email, firstName, lastName);

        assertTrue(creationResult.isUserAlreadyExists());
        assertEquals("john.doe@mail.com", creationResult.getExistingUserEmail());
    }

    @Test
    public void signUp_userCreationFails_shouldReturnTheThrownError() {
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";

        when(userRepositoryMock.createUser(eq(email), eq(firstName), eq(lastName))).thenThrow(new UserCreationException("some error"));
        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock);

        UserCreationResult creationResult = unit.signUp(email, firstName, lastName);

        assertFalse(creationResult.isSuccess());
        assertFalse(creationResult.isUserAlreadyExists());
        assertEquals("some error", creationResult.getError().getMessage());
    }
    @Test
    public void signUp_userValidationFails_shouldReturnTheThrownError() {
        String email = "john.doe@mail.com";
        String firstName = "";
        String lastName = "Doe";

        doThrow(new UserValidationException("invalid user")).when(userValidatorMock).validateUser(eq(email), eq(firstName), eq(lastName));
        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock);

        UserCreationResult creationResult = unit.signUp(email, firstName, lastName);

        assertFalse(creationResult.isSuccess());
        assertFalse(creationResult.isUserAlreadyExists());
        assertEquals("invalid user", creationResult.getError().getMessage());
    }

    @Test
    public void logIn_shouldReturnUserIfExists() {
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";
        User user = new User("someId", email, firstName, lastName);

        when(userRepositoryMock.findByEmail(eq(email))).thenReturn(Optional.of(user));
        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock);
        User foundUser = unit.logIn(email).get();

        assertNotNull(foundUser);
        assertEquals("john.doe@mail.com", foundUser.email());
        assertEquals("John", foundUser.firstName());
        assertEquals("Doe", foundUser.lastName());
    }

    @Test
    public void logIn_shouldReturnEmptyIfUserIsNotFound() {
        String email = "john.doe@mail.com";
        when(userRepositoryMock.findByEmail(eq(email))).thenReturn(Optional.empty());

        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock);
        Optional<User> foundUser = unit.logIn(email);

        assertTrue(foundUser.isEmpty());
    }
}

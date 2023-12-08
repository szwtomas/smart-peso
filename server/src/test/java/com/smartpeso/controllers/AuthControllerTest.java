package com.smartpeso.controllers;

import com.smartpeso.model.dto.auth.AuthenticationResponse;
import com.smartpeso.model.dto.auth.SignUpRequest;
import com.smartpeso.repositories.exceptions.UserCreationException;
import com.smartpeso.services.auth.AuthService;
import com.smartpeso.services.auth.UserCreationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AuthControllerTest {
    @Mock
    AuthService authServiceMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void signUp_givenUserIsCreatedCorrectly_shouldReturnCreatedStatusCodeAndUserData() {
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "somepassword";

        SignUpRequest signUpDTO = new SignUpRequest(email, password, firstName, lastName);
        AuthenticationResponse authResponse = new AuthenticationResponse("access-token", email, firstName, lastName);
        UserCreationResult userCreationResult = UserCreationResult.success(authResponse);
        when(authServiceMock.signUp(eq(email), eq(password), eq(firstName), eq(lastName))).thenReturn(userCreationResult);
        AuthController unit = new AuthController(authServiceMock);

        ResponseEntity<?> response = unit.signUp(signUpDTO);
        AuthenticationResponse actual = (AuthenticationResponse) response.getBody();

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(actual);
        assertEquals("access-token", actual.accessToken());
        assertEquals("john.doe@mail.com", actual.email());
        assertEquals("John", actual.firstName());
        assertEquals("Doe", actual.lastName());
    }

    @Test
    public void signUp_userAlreadyExists_shouldReturnConflictStatsCode() {
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "somepassword";

        SignUpRequest signUpDTO = new SignUpRequest(email, password, firstName, lastName);
        UserCreationResult userCreationResult = UserCreationResult.userAlreadyExists(email);
        when(authServiceMock.signUp(eq(email), eq(password), eq(firstName), eq(lastName))).thenReturn(userCreationResult);
        AuthController unit = new AuthController(authServiceMock);

        ResponseEntity<?> response = unit.signUp(signUpDTO);
        String message = (String) response.getBody();

        assertEquals(409, response.getStatusCode().value());
        assertNotNull(message);
        assertEquals("User with email john.doe@mail.com already exists", message);
    }

    @Test
    public void signUp_userCreationFails_shouldReturnInternalServerError() {
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "somepassword";

        SignUpRequest signUpDTO = new SignUpRequest(email, password, firstName, lastName);
        UserCreationResult userCreationResult = UserCreationResult.failure(new UserCreationException("some error"));
        when(authServiceMock.signUp(eq(email), eq(password), eq(firstName), eq(lastName))).thenReturn(userCreationResult);

        AuthController unit = new AuthController(authServiceMock);

        ResponseEntity<?> response = unit.signUp(signUpDTO);

        assertEquals(500, response.getStatusCode().value());
    }
}

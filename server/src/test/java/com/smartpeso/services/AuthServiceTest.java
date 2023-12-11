package com.smartpeso.services;

import com.smartpeso.model.User;
import com.smartpeso.model.dto.auth.AuthenticationResponse;
import com.smartpeso.repositories.exceptions.UserAlreadyExistsException;
import com.smartpeso.repositories.exceptions.UserCreationException;
import com.smartpeso.repositories.UserRepository;
import com.smartpeso.services.auth.AuthService;
import com.smartpeso.services.auth.JwtService;
import com.smartpeso.services.auth.UserCreationResult;
import com.smartpeso.validators.UserValidationException;
import com.smartpeso.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    @Mock
    private UserValidator userValidatorMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private AuthenticationManager authenticationManagerMock;
    @Mock
    private JwtService jwtServiceMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void signUp_userCreatedSuccessfully_shouldReturnSuccessResult() {
        int userId = 123;
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "password";
        String encodedPassword = "encoded-password";
        String role = "user";
        String accessToken = "access-token";

        when(passwordEncoderMock.encode(eq(password))).thenReturn(encodedPassword);
        doNothing().when(userRepositoryMock).createUser(eq(email), eq(encodedPassword), eq(role), eq(firstName), eq(lastName));
        when(jwtServiceMock.generateAccessToken(eq(email))).thenReturn(accessToken);

        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock, passwordEncoderMock, jwtServiceMock, authenticationManagerMock);

        UserCreationResult creationResult = unit.signUp(email, password, firstName, lastName);
        AuthenticationResponse actual = creationResult.getAuthenticationResponse();

        assertTrue(creationResult.isSuccess());
        assertEquals("access-token", actual.accessToken());
    }

    @Test
    public void signUp_userAlreadyExists_shouldReturnUserAlreadyExistsResult() {
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "password";
        String encodedPassword = "encoded-password";
        String role = "user";

        when(passwordEncoderMock.encode(password)).thenReturn(encodedPassword);
        doThrow(new UserAlreadyExistsException("some error")).when(userRepositoryMock).createUser(eq(email), eq(encodedPassword), eq(role), eq(firstName), eq(lastName));

        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock, passwordEncoderMock, jwtServiceMock, authenticationManagerMock);

        UserCreationResult creationResult = unit.signUp(email, password, firstName, lastName);

        assertTrue(creationResult.isUserAlreadyExists());
        assertEquals("john.doe@mail.com", creationResult.getExistingUserEmail());
    }

    @Test
    public void signUp_userCreationFails_shouldReturnTheThrownError() {
        String email = "john.doe@mail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "password";
        String encodedPassword = "encoded-password";
        String role = "user";

        when(passwordEncoderMock.encode(password)).thenReturn(encodedPassword);
        doThrow(new UserCreationException("some error")).when(userRepositoryMock).createUser(eq(email), eq(encodedPassword), eq(role), eq(firstName), eq(lastName));

        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock, passwordEncoderMock, jwtServiceMock, authenticationManagerMock);

        UserCreationResult creationResult = unit.signUp(email, password, firstName, lastName);

        assertFalse(creationResult.isSuccess());
        assertFalse(creationResult.isUserAlreadyExists());
        assertEquals("some error", creationResult.getError().getMessage());
    }
    @Test
    public void signUp_userValidationFails_shouldReturnTheThrownError() {
        String email = "john.doe@mail.com";
        String firstName = "";
        String lastName = "Doe";
        String password = "password";

        doThrow(new UserValidationException("invalid user")).when(userValidatorMock).validateUser(eq(email), eq(password), eq(firstName), eq(lastName));
        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock, passwordEncoderMock, jwtServiceMock, authenticationManagerMock);

        UserCreationResult creationResult = unit.signUp(email, password, firstName, lastName);

        assertFalse(creationResult.isSuccess());
        assertFalse(creationResult.isUserAlreadyExists());
        assertEquals("invalid user", creationResult.getError().getMessage());
    }

    @Test
    public void logIn_givenUserCredentialsAreValid_shouldReturnAuthenticationResponseWithUserDataAndAccessToken() {
        int userId = 123;
        String email = "john.doe@mail.com";
        String rawPassword = "password";

        User user = new User(userId, email, "encodedPassword", "user", "John", "Doe");
        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtServiceMock.generateAccessToken(email)).thenReturn("access-token");

        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock, passwordEncoderMock, jwtServiceMock, authenticationManagerMock);
        AuthenticationResponse authResponse = unit.authenticate(email, rawPassword);

        assertEquals("access-token", authResponse.accessToken());
    }

    @Test
    public void logIn_givenCredentialsAreInvalid_shouldThrowBadCredentialsException() {
        String email = "john.doe@mail.com";
        String rawPassword = "password";

        doThrow(new BadCredentialsException("bad credentials")).when(authenticationManagerMock).authenticate(any(Authentication.class));
        AuthService unit = new AuthService(userRepositoryMock, userValidatorMock, passwordEncoderMock, jwtServiceMock, authenticationManagerMock);

        assertThrows(BadCredentialsException.class, () -> unit.authenticate(email, rawPassword));
    }
}

package com.smartpeso.services;

import com.smartpeso.model.User;
import com.smartpeso.validators.UserValidationException;
import com.smartpeso.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserValidator userValidatorMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void userService_createUser_returnUserWithPassedData() {
        UserService unit = new UserService(userValidatorMock);

        User actual = unit.createUser("example@mail.com", "John", "Doe");

        assertEquals("example@mail.com", actual.email());
        assertEquals("John", actual.firstName());
        assertEquals("Doe", actual.lastName());
    }

    @Test
    public void userService_userValidationFails_shouldThrowUserInvalidException() {
        UserService unit = new UserService(userValidatorMock);

        when(unit.createUser(anyString(), anyString(), anyString())).thenThrow(new UserValidationException("Error"));

        assertThrows(UserValidationException.class, () -> {
           unit.createUser("", "", "");
        });
    }
}

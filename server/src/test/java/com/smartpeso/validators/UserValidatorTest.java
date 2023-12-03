package com.smartpeso.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {
    private final PasswordValidator passwordValidator = new PasswordValidator();

    private final UserValidator unit = new UserValidator(passwordValidator);

    @Test
    public void userValidator_correctEmail_shouldNotThrowAnException() {
        unit.validateUser("example@gmail.com", "Apassword123", "John", "Doe");
    }

    @Test
    public void userValidator_fieldsAreNull_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            unit.validateUser(null, "Apassword123", "John", "Doe");
        });
    }

    @Test
    public void userValidator_withTildes_doesNotThrowError() {
        unit.validateUser("example@mail.com", "Apassword123","Tomás", "Alavés");
    }

    @Test
    public void userValidator_emptyEmail_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("", "Apassword123", "John", "Doe");
        });
    }

    @Test
    public void userValidator_emailWithNoAt_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("examplegmail.com", "Apassword123", "John", "Doe");
        });
    }

    @Test
    public void userValidator_emailWithNoDomain_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@.com", "Apassword123", "John", "Doe");
        });
    }

    @Test
    public void userValidator_emailWithNoDomainExtension_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail", "Apassword123", "John", "Doe");
        });
    }

    @Test
    public void userValidator_emailWithNoFirstPart_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("@gmail.com", "Apassword123", "John", "Doe");
        });
    }

    @Test
    public void userValidator_firstNameEmpty_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "Apassword123", "", "Doe");
        });
    }

    @Test
    public void userValidator_lastNameEmpty_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "Apassword123", "John", "");
        });
    }

    @Test
    public void userValidator_nameWithOnly1Letter_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "Apassword123", "J", "Doe");
        });
    }

    @Test
    public void userValidator_nameWithNumbers_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "Apassword123", "John2", "Doe");
        });
    }

    @Test
    public void userValidator_nameWithSymbols_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "Apassword123", "J_ohn", "Doe");
        });
    }
}

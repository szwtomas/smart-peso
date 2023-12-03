package com.smartpeso.validators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {
    private final PasswordValidator passwordValidator = new PasswordValidator();
    private final UserValidator unit = new UserValidator(passwordValidator);

    @Test
    public void userValidator_correctEmail_shouldNotThrowAnException() {
        unit.validateUser("example@gmail.com", "John", "Doe");
    }

    @Test
    public void userValidator_fieldsAreNull_shouldThrowException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser(null, "John", "Doe");
        });
    }

    @Test
    public void userValidator_withTildes_doesNotThrowError() {
        unit.validateUser("example@mail.com", "Tomás", "Alavés");
    }

    @Test
    public void userValidator_emptyEmail_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("", "John", "Doe");
        });
    }

    @Test
    public void userValidator_emailWithNoAt_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("examplegmail.com", "John", "Doe");
        });
    }

    @Test
    public void userValidator_emailWithNoDomain_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@.com", "John", "Doe");
        });
    }

    @Test
    public void userValidator_emailWithNoDomainExtension_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail", "John", "Doe");
        });
    }

    @Test
    public void userValidator_emailWithNoFirstPart_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("@gmail.com", "John", "Doe");
        });
    }

    @Test
    public void userValidator_firstNameEmpty_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "", "Doe");
        });
    }

    @Test
    public void userValidator_lastNameEmpty_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "John", "");
        });
    }

    @Test
    public void userValidator_nameWithOnly1Letter_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "J", "Doe");
        });
    }

    @Test
    public void userValidator_nameWithNumbers_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "John2", "Doe");
        });
    }

    @Test
    public void userValidator_nameWithSymbols_shouldThrowUserValidatorException() {
        assertThrows(UserValidationException.class, () -> {
            unit.validateUser("example@gmail.com", "J_ohn", "Doe");
        });
    }
}

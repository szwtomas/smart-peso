package com.smartpeso.auth;

import com.smartpeso.auth.validators.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    public void setup() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    public void givenPasswordMeetsAllCriteria_itShouldPassValidation() {
        String password = "Passw0rd123";
        assertTrue(passwordValidator.isValidPassword(password));
    }

    @Test
    public void givenPasswordDoesNotMeetMinimumLength_itShouldFailValidation() {
        String password = "Pa0rd";
        assertFalse(passwordValidator.isValidPassword(password));
    }

    @Test
    public void givenPasswordDoesNotMeetMinimumDigitCount_itShouldFailValidation() {
        String password = "Password";
        assertFalse(passwordValidator.isValidPassword(password));
    }

    @Test
    public void givenPasswordDoesNotMeetMinimumLetterCount_itShouldFailValidation() {
        String password = "12345678";
        assertFalse(passwordValidator.isValidPassword(password));
    }

    @Test
    public void givenPasswordDoesNotMeetMinimumUpperCase_itShouldFailValidation() {
        String password = "passw0rd";
        assertFalse(passwordValidator.isValidPassword(password));
    }

    @Test
    public void givenPasswordMeetsLengthButFailsDigitCount_itShouldFailValidation() {
        String password = "Password";
        assertFalse(passwordValidator.isValidPassword(password));
    }

    @Test
    public void givenPasswordMeetsLengthButFailsLetterCount_itShouldFailValidation() {
        String password = "12345678";
        assertFalse(passwordValidator.isValidPassword(password));
    }

    @Test
    public void givenPasswordMeetsLengthButFailsUpperCase_itShouldFailValidation() {
        String password = "passw0rd";
        assertFalse(passwordValidator.isValidPassword(password));
    }

    @Test
    public void givenEmptyPassword_itShouldFailValidation() {
        String password = "";
        assertFalse(passwordValidator.isValidPassword(password));
    }

    @Test
    public void givenNullPassword_itShouldFailValidation() {
        assertFalse(passwordValidator.isValidPassword(null));
    }
}


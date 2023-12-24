package com.smartpeso.auth.validators;

import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class PasswordValidator {
    private static int PASSWORD_MIN_LENGTH = 8;
    private static int PASSWORD_MIN_NUMBERS = 2;
    private static int PASSWORD_MIN_LETTERS = 2;
    private static int PASSWORD_MIN_UPPERCASE = 1;

    public boolean isValidPassword(String password) {
        return password != null
                && hasMinimumLength(password)
                && hasMinimumDigitCount(password)
                && hasMinimumLetterCount(password)
                && hasMinimumUpperCase(password);
    }

    private boolean hasMinimumLength(String password) {
        return password.length() >= PASSWORD_MIN_LENGTH;
    }

    private boolean hasMinimumDigitCount(String password) {
        int digitCount = 0;
        for (char ch : password.toCharArray()) {
            if (Character.isDigit(ch)) {
                digitCount++;
            }
            if (digitCount >= PASSWORD_MIN_NUMBERS) {
                return true;
            }
        }
        return false;
    }

    private boolean hasMinimumLetterCount(String password) {
        int letterCount = 0;
        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) {
                letterCount++;
            }
            if (letterCount >= PASSWORD_MIN_LETTERS) {
                return true;
            }
        }
        return false;
    }

    private boolean hasMinimumUpperCase(String password) {
        int upperCaseCount = 0;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                upperCaseCount++;
            }
            if (upperCaseCount >= PASSWORD_MIN_UPPERCASE) {
                return true;
            }
        }
        return false;
    }
}

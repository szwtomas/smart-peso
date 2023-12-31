package com.smartpeso.auth.validators;

import com.smartpeso.auth.exception.UserValidationException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserValidator {
    private final static String VALID_EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private final static String VALID_NAME_REGEX = "^[a-zA-ZÁáÉéÍíÓóÚúÂâÊêÎîÔôÛûÇç ]+$";

    private final PasswordValidator passwordValidator;

    public UserValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }


    public void validateUser(@NonNull String email, @NonNull String password, @NonNull String firstName, @NonNull String lastName) {
        if (!passwordValidator.isValidPassword(password)) {
            throw new UserValidationException("Invalid password");
        }

        validateEmail(email);
        validateName(firstName);
        validateName(lastName);
    }

    private void validateName(String name) {
        if (name.length() <= 1) {
            throw new UserValidationException(String.format("Validation failed, name %s can not be shorter than 2 chars", name));
        } else if (!patternMatches(name, VALID_NAME_REGEX)) {
            throw new UserValidationException(String.format("Validation failed, name %s can't contain numbers or symbols", name));
        }
    }

    private void validateEmail(String email) {
        if (!patternMatches(email, VALID_EMAIL_REGEX)) {
            throw new UserValidationException(String.format("Validation failed, email %s is not valid", email));
        }
    }

    private boolean patternMatches(String s, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(s)
                .matches();
    }
}

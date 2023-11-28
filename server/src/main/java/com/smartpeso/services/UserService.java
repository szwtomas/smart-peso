package com.smartpeso.services;

import com.smartpeso.model.User;
import com.smartpeso.validators.UserValidator;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserValidator userValidator;
    public UserService(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    public User createUser(String email, String firstName, String lastName) {
        userValidator.validateUser(email, firstName, lastName);
        return new User(1, email, firstName, lastName);
    }
}

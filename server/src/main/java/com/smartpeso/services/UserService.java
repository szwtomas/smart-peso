package com.smartpeso.services;

import com.smartpeso.model.User;
import com.smartpeso.repositories.UserRepository;
import com.smartpeso.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserValidator userValidator;

    @Autowired
    public UserService(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    public User createUser(String email, String firstName, String lastName) {
        userValidator.validateUser(email, firstName, lastName);
        return new User(1, email, firstName, lastName);
    }
}

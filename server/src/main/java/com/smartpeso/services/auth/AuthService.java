package com.smartpeso.services.auth;

import com.smartpeso.model.User;
import com.smartpeso.repositories.UserAlreadyExistsException;
import com.smartpeso.repositories.UserCreationException;
import com.smartpeso.repositories.UserRepository;
import com.smartpeso.validators.UserValidationException;
import com.smartpeso.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Autowired
    public AuthService(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public UserCreationResult signUp(String email, String firstName, String lastName) {
        try {
            userValidator.validateUser(email, firstName, lastName);
            User user = userRepository.createUser(email, firstName, lastName);
            return UserCreationResult.success(user);
        } catch(UserAlreadyExistsException e) {
            return UserCreationResult.userAlreadyExists(email);
        } catch(UserValidationException | UserCreationException e) {
            return UserCreationResult.failure(e);
        }
    }

    public Optional<User> logIn(String email) {
        return userRepository.findByEmail(email);
    }
}

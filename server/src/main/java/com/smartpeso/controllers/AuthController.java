package com.smartpeso.controllers;

import com.smartpeso.model.User;
import com.smartpeso.model.dto.auth.LogInDTO;
import com.smartpeso.model.dto.auth.SignUpDTO;
import com.smartpeso.services.auth.AuthService;
import com.smartpeso.services.auth.UserCreationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO) {
        UserCreationResult creationResult = authService.signUp(
                signUpDTO.email(),
                signUpDTO.firstName(),
                signUpDTO.lastName()
        );

        if (creationResult.isSuccess()) {
            User createdUser = creationResult.getUser();
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } else if (creationResult.isUserAlreadyExists()) {
            String message = String.format("User with email %s already exists", creationResult.getExistingUserEmail());
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            String errorMessage = creationResult.getError().getMessage();
            log.error(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/log-in")
    public ResponseEntity<?> logIn(@RequestBody LogInDTO logInDTO) {
        String email = logInDTO.email();
        Optional<User> user = authService.logIn(email);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong email/password combination", HttpStatus.NOT_FOUND);
        }
    }
}

package com.smartpeso.controllers;

import com.smartpeso.model.User;
import com.smartpeso.model.dto.auth.AuthenticationResponse;
import com.smartpeso.model.dto.auth.LogInRequest;
import com.smartpeso.model.dto.auth.SignUpRequest;
import com.smartpeso.services.auth.AuthService;
import com.smartpeso.services.auth.UserCreationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        UserCreationResult creationResult = authService.signUp(
                signUpRequest.email(),
                signUpRequest.password(),
                signUpRequest.firstName(),
                signUpRequest.lastName()
        );

        if (creationResult.isSuccess()) {
            log.info("User with email " + signUpRequest.email() + " created");
            return new ResponseEntity<>(creationResult.getAuthenticationResponse(), HttpStatus.CREATED);
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
    public ResponseEntity<?> logIn(@RequestBody LogInRequest logInRequest) {
        try {
            AuthenticationResponse authenticationResponse = authService.authenticate(logInRequest.email(), logInRequest.password());
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
        } catch(BadCredentialsException e) {
            log.error("Invalid credentials: " + e.getMessage());
            return new ResponseEntity<>("Wrong email/password combination", HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            log.error("Unexpected error on login: " + e.getMessage());
            return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userdata")
    public String userData(@AuthenticationPrincipal User user) {
        String userId = user.getUserId();
        String role = String.valueOf(user.getAuthorities().stream().toList().get(0));
        return userId + " " + role;
    }
}

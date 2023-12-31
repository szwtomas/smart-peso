package com.smartpeso.auth.controller;

import com.smartpeso.auth.model.User;
import com.smartpeso.auth.model.UserCreationResult;
import com.smartpeso.auth.model.dto.AuthenticationResponse;
import com.smartpeso.auth.model.dto.LogInRequest;
import com.smartpeso.auth.model.dto.SignUpRequest;
import com.smartpeso.auth.model.dto.UserDTO;
import com.smartpeso.auth.service.AuthService;
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

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserData(@AuthenticationPrincipal User user) {
        UserDTO userDTO = new UserDTO(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}

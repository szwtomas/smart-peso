package com.smartpeso.services.auth;

import com.smartpeso.model.User;
import com.smartpeso.model.dto.auth.AuthenticationResponse;
import com.smartpeso.repositories.exceptions.UserAlreadyExistsException;
import com.smartpeso.repositories.exceptions.UserCreationException;
import com.smartpeso.repositories.UserRepository;
import com.smartpeso.validators.UserValidationException;
import com.smartpeso.validators.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            UserValidator userValidator,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public UserCreationResult signUp(String email, String rawPassword, String firstName, String lastName) {
        try {
            userValidator.validateUser(email, rawPassword, firstName, lastName);
            String encodedPassword = passwordEncoder.encode(rawPassword);
            User user = userRepository.createUser(email, encodedPassword, "user", firstName, lastName);
            String accessToken = jwtService.generateAccessToken(email);
            AuthenticationResponse response = new AuthenticationResponse(accessToken, email, user.getFirstName(), user.getLastName());
            return UserCreationResult.success(response);
        } catch(UserAlreadyExistsException e) {
            return UserCreationResult.userAlreadyExists(email);
        } catch(UserValidationException | UserCreationException e) {
            return UserCreationResult.failure(e);
        }
    }

    public AuthenticationResponse authenticate(String email, String rawPassword) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, rawPassword);
        authenticationManager.authenticate(authentication);
        User user = userRepository.findByEmail(email).orElseThrow();
        String accessToken = jwtService.generateAccessToken(email);
        return new AuthenticationResponse(accessToken, email, user.getFirstName(), user.getLastName());
    }
}

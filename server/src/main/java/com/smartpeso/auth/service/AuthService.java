package com.smartpeso.auth.service;

import com.smartpeso.auth.dal.UserRepository;
import com.smartpeso.auth.model.User;
import com.smartpeso.auth.model.UserCreationResult;
import com.smartpeso.auth.model.dto.AuthenticationResponse;
import com.smartpeso.auth.exception.UserAlreadyExistsException;
import com.smartpeso.auth.exception.UserCreationException;
import com.smartpeso.auth.exception.UserValidationException;
import com.smartpeso.auth.validators.UserValidator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    @Setter
    @Value("${app-properties.pepper}")
    private String pepper;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final SaltGenerator saltGenerator;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            UserValidator userValidator,
            PasswordEncoder passwordEncoder,
            SaltGenerator saltGenerator,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.saltGenerator = saltGenerator;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public UserCreationResult signUp(String email, String rawPassword, String firstName, String lastName) {
        try {
            userValidator.validateUser(email, rawPassword, firstName, lastName);
            String salt = saltGenerator.generateSalt();
            String saltedPassword = rawPassword + salt + pepper;
            String encodedPassword = passwordEncoder.encode(saltedPassword);
            userRepository.createUser(email, encodedPassword, salt, "user", firstName, lastName);
            String accessToken = jwtService.generateAccessToken(email);
            AuthenticationResponse response = new AuthenticationResponse(accessToken);
            return UserCreationResult.success(response);
        } catch(UserAlreadyExistsException e) {
            return UserCreationResult.userAlreadyExists(email);
        } catch(UserValidationException | UserCreationException e) {
            return UserCreationResult.failure(e);
        }
    }

    public AuthenticationResponse authenticate(String email, String rawPassword) {
        String salt = getUserSalt(email);
        String saltedPassword = rawPassword + salt + pepper;
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, saltedPassword);
        authenticationManager.authenticate(authentication);
        String accessToken = jwtService.generateAccessToken(email);
        return new AuthenticationResponse(accessToken);
    }

    private String getUserSalt(String email) {
        return userRepository
                .findByEmail(email)
                .map(User::getSalt)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }
}

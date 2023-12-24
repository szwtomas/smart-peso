package com.smartpeso.auth.model;

import com.smartpeso.auth.model.dto.AuthenticationResponse;
import lombok.Getter;

@Getter
public class UserCreationResult {
    private final AuthenticationResponse authenticationResponse;
    private final Throwable error;
    private final String existingUserEmail;

    private UserCreationResult(AuthenticationResponse authenticationResponse, Throwable error, String existingUserEmail) {
        this.authenticationResponse = authenticationResponse;
        this.error = error;
        this.existingUserEmail = existingUserEmail;
    }

    public static UserCreationResult success(AuthenticationResponse authenticationResponse) {
        return new UserCreationResult(authenticationResponse,  null, null);
    }

    public static UserCreationResult failure(Throwable error) {
        return new UserCreationResult(null, error, null);
    }

    public static UserCreationResult userAlreadyExists(String email) {
        return new UserCreationResult(null, null, email);
    }

    public boolean isSuccess() {
        return this.authenticationResponse != null && this.error == null && this.existingUserEmail == null;
    }

    public boolean isUserAlreadyExists() {
        return this.existingUserEmail != null;
    }
}

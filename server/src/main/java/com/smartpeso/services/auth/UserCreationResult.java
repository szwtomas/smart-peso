package com.smartpeso.services.auth;

import com.smartpeso.model.User;
import lombok.Getter;

@Getter
public class UserCreationResult {
    private final User user;
    private final Throwable error;
    private final String existingUserEmail;

    private UserCreationResult(User user, Throwable error, String existingUserEmail) {
        this.user = user;
        this.error = error;
        this.existingUserEmail = existingUserEmail;
    }

    public static UserCreationResult success(User user) {
        return new UserCreationResult(user,  null, null);
    }

    public static UserCreationResult failure(Throwable error) {
        return new UserCreationResult(null, error, null);
    }

    public static UserCreationResult userAlreadyExists(String email) {
        return new UserCreationResult(null, null, email);
    }

    public boolean isSuccess() {
        return this.error == null && this.existingUserEmail == null;
    }

    public boolean isUserAlreadyExists() {
        return this.existingUserEmail != null;
    }
}

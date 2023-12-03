package com.smartpeso.services.auth;

public class UserAuthenticationFailedException extends RuntimeException {
    public UserAuthenticationFailedException(String message) {
        super(message);
    }
}

package com.smartpeso.model.dto.auth;

public record SignUpRequest(String email, String password, String firstName, String lastName) {
}

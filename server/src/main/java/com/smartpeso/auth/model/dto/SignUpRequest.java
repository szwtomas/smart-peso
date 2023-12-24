package com.smartpeso.auth.model.dto;

public record SignUpRequest(String email, String password, String firstName, String lastName) {
}

package com.smartpeso.services.auth;

import lombok.Setter;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class SaltGenerator {
    private final static int SALT_LENGTH = 16;

    private final SecureRandom random;

    public SaltGenerator(SecureRandom random) {
        this.random = random;
    }

    public String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}

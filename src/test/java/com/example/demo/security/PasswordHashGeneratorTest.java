package com.example.demo.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHashGeneratorTest {

    @Test
    void generatesKnownDevPasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("Gourmet123!");
        System.out.println("BCrypt hash for Gourmet123!: " + hash);
        assertTrue(encoder.matches("Gourmet123!", hash));
    }
}

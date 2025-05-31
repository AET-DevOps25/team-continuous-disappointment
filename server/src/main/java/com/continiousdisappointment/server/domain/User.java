package com.continiousdisappointment.server.domain;

public record User(Integer id, String username, String email) {
    public User {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID must be a non-negative integer.");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }
    }
}

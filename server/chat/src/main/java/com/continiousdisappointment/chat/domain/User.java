package com.continiousdisappointment.chat.domain;

public record User(Integer id, String username) {
    public User {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID must be a non-negative integer.");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank.");
        }
    }
}

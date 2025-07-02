package com.continiousdisappointment.chat.domain;

import java.util.Set;

public record User(Integer id, String username, Set<DietaryPreference> dietaryPreferences) {
    public User {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID must be a non-negative integer.");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank.");
        }
        if (dietaryPreferences == null) {
            throw new IllegalArgumentException("Preferences cannot be null or empty.");
        }
    }
}

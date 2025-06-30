package com.continiousdisappointment.user.domain;

import java.util.Set;

import org.springframework.lang.NonNull;

public class User extends OAuthUser {
    private final Set<DietaryPreference> dietaryPreferences;

    public User(String username, Integer id, @NonNull Set<DietaryPreference> dietaryPreferences) {
        super(username, id);
        this.dietaryPreferences = dietaryPreferences;
    }

    public Set<DietaryPreference> getDietaryPreferences() {
        return dietaryPreferences;
    }
}

package com.continiousdisappointment.user.dto;

import java.util.Set;

import com.continiousdisappointment.user.domain.DietaryPreference;

public record SaveUserPreferencesRequestDto(Set<DietaryPreference> dietaryPreferences) {

    public SaveUserPreferencesRequestDto {
        if (dietaryPreferences == null) {
            throw new IllegalArgumentException("Dietary preferences cannot be null or empty");
        }
    }
}

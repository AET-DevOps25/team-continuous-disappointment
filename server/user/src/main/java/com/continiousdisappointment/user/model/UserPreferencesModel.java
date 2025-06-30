package com.continiousdisappointment.user.model;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.continiousdisappointment.user.domain.DietaryPreference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user-preferences")
public class UserPreferencesModel {
    @Id
    public UUID id;
    public int userId;
    public Set<DietaryPreference> dietaryPreferences;
}

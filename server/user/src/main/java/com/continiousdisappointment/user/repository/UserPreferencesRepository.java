package com.continiousdisappointment.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import com.continiousdisappointment.user.model.UserPreferencesModel;

public interface UserPreferencesRepository extends MongoRepository<UserPreferencesModel, UUID> {

    List<UserPreferencesModel> findByUserId(int userId);

    Optional<UserPreferencesModel> findById(@NonNull UUID id);
}

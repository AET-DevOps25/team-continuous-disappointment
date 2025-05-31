package com.continiousdisappointment.server.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import com.continiousdisappointment.server.model.Chat;

public interface ChatRepository extends MongoRepository<Chat, UUID> {

    List<Chat> findByUserId(int userId);

    Optional<Chat> findById(@NonNull UUID id);

}

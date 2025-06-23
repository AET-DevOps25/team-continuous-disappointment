package com.continiousdisappointment.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import com.continiousdisappointment.chat.model.ChatModel;

public interface ChatRepository extends MongoRepository<ChatModel, UUID> {

    List<ChatModel> findByUserId(int userId);

    Optional<ChatModel> findById(@NonNull UUID id);

}

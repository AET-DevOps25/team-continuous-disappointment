package com.continiousdisappointment.server.domain.chat;

import com.continiousdisappointment.server.model.ChatModel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Chat(
        UUID id,
        Integer userId,
        String title,
        Instant createdAt,
        Instant updatedAt,
        List<Message> messages) {

    public Chat {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }
        if (userId == null || userId < 0) {
            throw new IllegalArgumentException("User ID must be a non-negative integer.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank.");
        }
        if (createdAt == null || updatedAt == null) {
            throw new IllegalArgumentException("Timestamps cannot be null.");
        }
    }

    public static Chat fromDom(ChatModel chatModel) {
        return new Chat(
                chatModel.getId(),
                chatModel.getUserId(),
                chatModel.getTitle(),
                chatModel.getCreatedAt(),
                chatModel.getUpdatedAt(),
                chatModel.getMessages().stream()
                        .map(Message::fromDom)
                        .toList());

    }

}

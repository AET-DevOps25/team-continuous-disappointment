package com.continiousdisappointment.server.domain.chat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Conversation(
        UUID id,
        Integer userId,
        String title,
        Instant createdAt,
        Instant updatedAt,
        List<Message> messages) {

    public Conversation {
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

}

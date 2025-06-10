package com.continiousdisappointment.chat.domain.chat;

import java.time.Instant;
import java.util.UUID;

import com.continiousdisappointment.chat.model.MessageModel;

public record Message(UUID id, String content, Instant timestamp, Role role) {

    public Message {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank.");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
    }

    public static Message fromDom(MessageModel messageModel) {
        return new Message(
                messageModel.getId(),
                messageModel.getContent(),
                messageModel.getTimestamp(),
                Role.valueOf(messageModel.getRole().name()));
    }

}

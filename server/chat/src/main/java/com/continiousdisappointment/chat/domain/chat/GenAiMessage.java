package com.continiousdisappointment.chat.domain.chat;

public record GenAiMessage(Role role, String content) {
    public GenAiMessage {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank.");
        }
    }
}

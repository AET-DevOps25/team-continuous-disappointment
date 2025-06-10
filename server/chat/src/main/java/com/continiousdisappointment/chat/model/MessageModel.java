package com.continiousdisappointment.chat.model;

import java.time.Instant;
import java.util.UUID;

import com.continiousdisappointment.chat.domain.chat.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageModel {
    public UUID id;
    public String content;
    public Instant timestamp;
    public Role role;

    public MessageModel(String content, Role role) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.timestamp = Instant.now();
        this.role = role;
    }
}

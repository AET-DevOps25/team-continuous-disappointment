package com.continiousdisappointment.server.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chats")
public class ChatModel {
    @Id
    public UUID id;

    public int userId;

    public String title;

    @CreatedDate
    public Instant createdAt;

    @LastModifiedDate
    public Instant updatedAt;

    public List<MessageModel> messages;

    public ChatModel(UUID id, int userId, String title) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.messages = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

}

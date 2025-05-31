package com.continiousdisappointment.server.model;

import java.time.Instant;
import java.util.UUID;

import com.continiousdisappointment.server.domain.chat.Role;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Message {
    public UUID id;
    public String content;
    public Instant timestamp;
    public Role role;
}

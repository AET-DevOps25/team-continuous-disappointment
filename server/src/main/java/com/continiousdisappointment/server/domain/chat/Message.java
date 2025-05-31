package com.continiousdisappointment.server.domain.chat;

import java.time.Instant;
import java.util.UUID;

public record Message(UUID id, String content, Instant timestamp, Role role) {

}

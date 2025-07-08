package com.continiousdisappointment.chat.dto;

import com.continiousdisappointment.chat.domain.chat.GenAiMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GenAiRequest(
        @JsonProperty("query") String query,
        @JsonProperty("messages")List<GenAiMessage> messages,
        @JsonProperty("user_id") Integer userId
) {}

package com.continiousdisappointment.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenAiResponse(
@JsonProperty("response") String response
) {}

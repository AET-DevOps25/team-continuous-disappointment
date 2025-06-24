package com.continiousdisappointment.chat.service;

import com.continiousdisappointment.chat.domain.chat.GenAiMessage;
import com.continiousdisappointment.chat.dto.GenAiRequest;
import com.continiousdisappointment.chat.dto.GenAiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GenAiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateAssistantReply(String query, List<GenAiMessage> messages) {
        GenAiRequest request = new GenAiRequest(query, messages);
        ResponseEntity<GenAiResponse> response = restTemplate.postForEntity(
                "http://genai-service/genai/generate",
                request,
                GenAiResponse.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("GenAI service responded with an error");
        }
        return response.getBody().response();
    }
}

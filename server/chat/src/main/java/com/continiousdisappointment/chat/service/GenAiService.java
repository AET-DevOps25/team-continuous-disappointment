package com.continiousdisappointment.chat.service;

import com.continiousdisappointment.chat.domain.chat.GenAiMessage;
import com.continiousdisappointment.chat.dto.GenAiRequest;
import com.continiousdisappointment.chat.dto.GenAiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class GenAiService {
    private final Environment environment;
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateAssistantReply(String query, List<GenAiMessage> messages, Integer userId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            GenAiRequest genAiRequest = new GenAiRequest(query, messages, userId);
            HttpEntity<GenAiRequest> entity = new HttpEntity<>(genAiRequest, headers);

            log.info("Calling GenAI service");
            ResponseEntity<GenAiResponse> response = restTemplate.postForEntity(
                    getGenAiServiceUrl() + "/genai/generate",
                    entity,
                    GenAiResponse.class);
            log.info("GenAI service call successful");
            return response.getBody().response();

        } catch (Exception e) {
            log.error("Error calling GenAI service: {}", e.getMessage());
            return "";
        }
    }

    private String getGenAiServiceUrl() {
        if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
            return "http://localhost:8000";
        }
        return "http://genai-service:8000";
    }
}

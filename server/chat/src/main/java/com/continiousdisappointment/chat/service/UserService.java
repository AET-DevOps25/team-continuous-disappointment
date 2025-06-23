package com.continiousdisappointment.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.env.Environment;

import com.continiousdisappointment.chat.domain.User;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final RestTemplate restTemplate = new RestTemplate();
    private final Environment environment;

    public User getUserInfo(String authorization) {
        String url = getUserServiceUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, authorization);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                User.class);
        return response.getBody();
    }

    private String getUserServiceUrl() {
        if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
            return "http://localhost:8081/info";
        }
        return "http://user-service/info";
    }
}

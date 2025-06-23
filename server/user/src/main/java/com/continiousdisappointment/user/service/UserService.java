package com.continiousdisappointment.user.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.continiousdisappointment.user.domain.User;

@Service
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();

    public User getUserInfo(String authorization) {
        String url = "https://gitlab.lrz.de/api/v4/user";
        if (authorization == null) {
            throw new IllegalStateException("No access token found in security context");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization); // Sets Authorization: Bearer <token>

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                User.class);
        return response.getBody();
    }
}

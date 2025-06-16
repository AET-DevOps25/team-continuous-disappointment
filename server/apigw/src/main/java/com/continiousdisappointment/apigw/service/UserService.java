package com.continiousdisappointment.apigw.service;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.continiousdisappointment.user.domain.User;

@Service
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();

    public User getUserInfo() {
        String url = "https://gitlab.lrz.de/api/v4/user";
        String accessToken = getBearerToken();
        if (accessToken == null) {
            throw new IllegalStateException("No access token found in security context");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Sets Authorization: Bearer <token>

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                User.class);
        return response.getBody();
    }

    private String getBearerToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        OAuth2AccessToken credentials = (OAuth2AccessToken) authentication.getCredentials();
        if (credentials != null) {
            return credentials.getTokenValue();
        }
        return null;
    }

}

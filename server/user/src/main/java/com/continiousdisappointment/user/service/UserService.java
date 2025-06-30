package com.continiousdisappointment.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Set;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.continiousdisappointment.user.domain.DietaryPreference;
import com.continiousdisappointment.user.domain.OAuthUser;
import com.continiousdisappointment.user.domain.User;
import com.continiousdisappointment.user.model.UserPreferencesModel;
import com.continiousdisappointment.user.repository.UserPreferencesRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserPreferencesRepository userPreferencesRepository;

    public User getUserInfo(String authorization) {
        OAuthUser oAuthUser = getOAuthUserInfo(authorization);
        var userPreferences = getUserPreferences(authorization);
        return new User(oAuthUser.getUsername(), oAuthUser.getId(), userPreferences);
    }

    public void saveUserPreferences(String authorization, Set<DietaryPreference> dietaryPreferences) {
        OAuthUser oAuthUser = getOAuthUserInfo(authorization);
        var userPreferences = new UserPreferencesModel();
        userPreferences.setUserId(oAuthUser.getId());
        userPreferences.setDietaryPreferences(dietaryPreferences);
        userPreferencesRepository.save(userPreferences);
    }

    private Set<DietaryPreference> getUserPreferences(String authorization) {
        OAuthUser oAuthUser = getOAuthUserInfo(authorization);
        return userPreferencesRepository.findByUserId(oAuthUser.getId()).stream()
                .findFirst()
                .map(UserPreferencesModel::getDietaryPreferences)
                .orElse(Set.of());
    }

    private OAuthUser getOAuthUserInfo(String authorization) {
        String url = "https://gitlab.lrz.de/api{/v4/user";
        if (authorization == null) {
            log.warn("No access token found in security context");
            throw new IllegalStateException("No access token found in security context");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization); // Sets Authorization: Bearer <token>

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<OAuthUser> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                OAuthUser.class);
        return response.getBody();
    }
}

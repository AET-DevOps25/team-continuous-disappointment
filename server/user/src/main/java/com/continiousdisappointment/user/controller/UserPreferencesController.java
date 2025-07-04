package com.continiousdisappointment.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.continiousdisappointment.user.dto.SaveUserPreferencesRequestDto;
import com.continiousdisappointment.user.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("user/preferences")
@Log4j2
@RequiredArgsConstructor
@Tag(name = "User Preferences Controller", description = "Handles user dietary preferences, requires bearer authorization")
public class UserPreferencesController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Object> saveUserPreferences(@RequestHeader("Authorization") String authorization,
            @RequestBody SaveUserPreferencesRequestDto request) {
        try {
            userService.saveUserPreferences(authorization, request.dietaryPreferences());

        } catch (Exception e) {
            log.warn("Error while saving user preferences", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }
}

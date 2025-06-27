package com.continiousdisappointment.user.controller;

import com.continiousdisappointment.user.domain.User;
import com.continiousdisappointment.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("info")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Returns user information, requires bearer authorization")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String authorization) {
        try {
            return ResponseEntity.ok(userService.getUserInfo(authorization));
        } catch (Exception e) {
            log.warn("Error while getting user info", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

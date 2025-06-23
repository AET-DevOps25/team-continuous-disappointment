package com.continiousdisappointment.user.controller;

import com.continiousdisappointment.user.domain.User;
import com.continiousdisappointment.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("info")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public User getUserInfo() {
        return userService.getUserInfo();
    }
}

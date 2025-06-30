package com.continiousdisappointment.user.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OAuthUser {
    private final String username;
    private final Integer id;
}

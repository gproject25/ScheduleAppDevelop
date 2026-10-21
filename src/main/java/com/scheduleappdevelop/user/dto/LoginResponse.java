package com.scheduleappdevelop.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private final Long id;
    private final String name;
    private final String email;
}

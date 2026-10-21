package com.scheduleappdevelop.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SessionUser {
    private final Long id;
    private final String name;
    private final String email;
}

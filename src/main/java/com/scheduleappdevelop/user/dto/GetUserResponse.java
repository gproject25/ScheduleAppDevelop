package com.scheduleappdevelop.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GetUserResponse {
    private final Long id;
    private final String email;
    private final LocalDateTime createAt;
    private final LocalDateTime modifiedAt;
}

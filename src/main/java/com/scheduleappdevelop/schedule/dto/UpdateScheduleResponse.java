package com.scheduleappdevelop.schedule.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UpdateScheduleResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String user;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}

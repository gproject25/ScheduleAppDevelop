package com.scheduleappdevelop.schedule.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CreateScheduleResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String userName;
    private final LocalDateTime createAt;
    private final LocalDateTime modifiedAt;
}

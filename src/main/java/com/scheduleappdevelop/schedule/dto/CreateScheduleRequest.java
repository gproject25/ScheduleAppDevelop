package com.scheduleappdevelop.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateScheduleRequest {
    @NotBlank(message = "제목이 공백일 수 없습니다")
    private String title;
    @NotBlank(message = "내용이 공백일 수 없습니다")
    private String content;
}

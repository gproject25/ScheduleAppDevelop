package com.scheduleappdevelop.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCommentRequest {
    @NotBlank(message = "내용이 공백일 수 없습니다")
    private String content;
}

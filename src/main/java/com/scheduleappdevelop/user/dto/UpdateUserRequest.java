package com.scheduleappdevelop.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserRequest {
    @NotBlank(message = "이름이 공백일 수 없습니다")
    private String name;
    @NotBlank(message = "이메일이 공백일 수 없습니다")
    @Email(message = "email 형식으로 입력하세요")
    private String email;
}

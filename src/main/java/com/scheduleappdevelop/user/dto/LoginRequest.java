package com.scheduleappdevelop.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "email 형식으로 입력하세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 8, message = "비밀번호는 8글자 이상이어야 합니다")
    @Pattern(regexp = "^\\S+$", message = "비밀번호에 공백을 사용할 수 없습니다")
    private String password;
}

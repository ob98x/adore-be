package com.authservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailVerificationDto {

    @Schema(description = "이메일", example = "dyw1014@gachon.ac.kr")
    @NotBlank
    @Email
    private final String email;

    @Schema(description = "인증 코드", example = "1234")
    @NotBlank
    private final String code;


    @Builder
    public EmailVerificationDto(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public static EmailVerificationDto of(String email, String code) {
        return EmailVerificationDto.builder()
                .email(email)
                .code(code)
                .build();
    }
}

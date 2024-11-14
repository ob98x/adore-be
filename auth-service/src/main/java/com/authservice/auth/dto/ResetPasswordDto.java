package com.authservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    @Schema(description = "이메일", example = "dyw1014@gachon.ac.kr")
    @NotBlank
    private String email;

    @Schema(description = "새 비밀번호", example = "password")
    @NotBlank
    private String newPassword;

    @Schema(description = "새 비밀번호 확인", example = "password")
    @NotBlank
    private String newPasswordConfirm;

    @Schema(description = "이메일 인증 여부", example = "true")
    @NotBlank
    private String emailVerify;
    @Builder
    public ResetPasswordDto(String email, String newPassword, String newPasswordConfirm, String emailVerify) {
        this.email = email;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
        this.emailVerify = emailVerify;
    }
    public static ResetPasswordDto of(String email, String newPassword, String newPasswordConfirm, String emailVerify) {
        return ResetPasswordDto.builder()
                .email(email)
                .newPassword(newPassword)
                .newPasswordConfirm(newPasswordConfirm)
                .emailVerify(emailVerify)
                .build();
    }
}

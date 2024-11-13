package com.authservice.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    private String email;
    private String newPassword;
    private String newPasswordConfirm;
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

package com.authservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignUpRequestDto {

    @Schema(description = "이메일", example = "dyw1014@gahon.ac.kr")
    @NotBlank
    @Email
    private final String email;

    @Schema(description = "이름", example = "김용우")
    @NotBlank
    private final String name;

    @Schema(description = "비밀번호", example = "password")
    @NotBlank
    private final String password;

    @Schema(description = "생년월일", example = "2000-10-14")
    @NotBlank
    private final LocalDate birthDate;

    @Schema(description = "닉네임", example = "용우")
    @NotBlank
    private final String nickname;

    @Schema(description = "약관 동의 여부", example = "true")
    @NotBlank
    private final boolean agreeTerms;

    @Schema(description = "유입 경로", example = "google")
    @NotBlank
    private final String inflow;

    @Schema(description="성별", example="남")
    @NotBlank
    private final String gender;

    @Schema(description = "닉네임 중복 여부", example = "false")
    @NotBlank
    private final boolean nicknameDuplicate;

    @Schema(description = "이메일 중복 여부", example = "false")
    @NotBlank
    private final boolean emailDuplicate;

    @Schema(description = "이메일 인증 여부", example = "true")
    @NotBlank
    private final boolean emailVerify;


    @Builder
    public SignUpRequestDto(
            String name, String email, String password, LocalDate birthDate, String nickname, boolean agreeTerms, String inflow, String gender, boolean nicknameDuplicate, boolean emailDuplicate, boolean emailVerify
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.nickname = nickname;
        this.agreeTerms = agreeTerms;
        this.inflow = inflow;
        this.gender = gender;
        this.nicknameDuplicate = nicknameDuplicate;
        this.emailDuplicate = emailDuplicate;
        this.emailVerify = emailVerify;
    }

    public static SignUpRequestDto of(String name, String email, String password, LocalDate birthDate, String nickname, boolean agreeTerms, String inflow, String gender, boolean nicknameDuplicate, boolean emailDuplicate, boolean emailVerify) {
        return SignUpRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .birthDate(birthDate)
                .nickname(nickname)
                .agreeTerms(agreeTerms)
                .inflow(inflow)
                .gender(gender)
                .nicknameDuplicate(nicknameDuplicate)
                .emailDuplicate(emailDuplicate)
                .emailVerify(emailVerify)
                .build();
    }
}
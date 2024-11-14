package com.userservice.user.dto;


import com.userservice.user.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetMyPageResponseDto {

    @Schema(description = "이름", example = "홍길동")
    private final String name;

    @Schema(description = "닉네임", example = "홍길동")
    private final String nickname;

    @Schema(description = "이메일", example = "dyw1014@gachon.ac.kr")
    private final String email;

    @Schema(description = "생년월일", example = "2000-01-01")
    private final LocalDate birthDate;

    @Schema(description = "성별", example = "남")
    private final String gender;

    @Builder
    public GetMyPageResponseDto(String name, String nickname, String email, LocalDate birthDate, String gender) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public static GetMyPageResponseDto of(String name, String nickname, String email, LocalDate birthDate, String gender) {
        return GetMyPageResponseDto.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .birthDate(birthDate)
                .gender(gender)
                .build();
    }

    public static GetMyPageResponseDto fromMember(Member member) {
        return GetMyPageResponseDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birthDate(member.getBirthDate())
                .gender(member.getGender())
                .build();
    }

}

package com.userservice.user.dto;


import com.userservice.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetMyPageResponseDto {
    private final String name;
    private final String nickname;
    private final String email;
    private final LocalDate birthDate;
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

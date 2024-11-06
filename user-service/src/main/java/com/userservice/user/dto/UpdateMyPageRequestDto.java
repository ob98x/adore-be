package com.userservice.user.dto;

import com.userservice.user.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateMyPageRequestDto {
    private String name;
    private String nickname;
    private String email;
    private LocalDate birthDate;

    @Builder
    public UpdateMyPageRequestDto(String name, String nickname, String email, LocalDate birthDate) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.birthDate = birthDate;
    }

    public static UpdateMyPageRequestDto of(String name, String nickname, String email, LocalDate birthDate) {
        return UpdateMyPageRequestDto.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .birthDate(birthDate)
                .build();
    }
    public static Member updateMember(Member member, UpdateMyPageRequestDto updateMyPageRequestDto) {
        BeanUtils.copyProperties(updateMyPageRequestDto, member); // 필요한 경우 비밀번호 제외
        return member;
    }

}

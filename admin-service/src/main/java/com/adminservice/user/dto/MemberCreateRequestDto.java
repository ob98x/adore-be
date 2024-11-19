package com.adminservice.user.dto;

import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberRole;
import com.adminservice.user.entity.MemberState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
public class MemberCreateRequestDto {

    @Schema(description = "회원 이름", example = "홍길동")
    private String name;

    @Schema(description = "회원 이메일", example = "이메일")
    private String email;

    @Schema(description = "회원 비밀번호", example = "비밀번호")
    private String password;

    @Schema(description = "회원 닉네임", example = "닉네임")
    private LocalDate birthDate;

    @Schema(description = "회원 성별", example = "남성")
    private String inflow;

    @Schema(description = "회원 생년월일", example = "1990-01-01")
    private String gender;

    @Schema(description = "회원 권한", example = "USER")
    private String nickname;

    @Schema(description = "회원 상태", example = "ACTIVE")
    private MemberState state;

    @Schema(description = "회원 권한", example = "USER")
    private MemberRole role;

    public static Member createMember(MemberCreateRequestDto memberCreateRequestDto) {
        return Member.builder()
                .name(memberCreateRequestDto.getName())
                .email(memberCreateRequestDto.getEmail())
                .password(memberCreateRequestDto.getPassword())
                .birthDate(memberCreateRequestDto.getBirthDate())
                .inflow(memberCreateRequestDto.getInflow())
                .gender(memberCreateRequestDto.getGender())
                .nickname(memberCreateRequestDto.getNickname())
                .state(memberCreateRequestDto.getState())
                .role(memberCreateRequestDto.getRole())
                .build();
    }

    public static Member updateMember(Member member, MemberCreateRequestDto memberCreateRequestDto) {
        BeanUtils.copyProperties(memberCreateRequestDto, member); // 필요한 경우 비밀번호 제외
        return member;
    }
}
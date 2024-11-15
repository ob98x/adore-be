package com.adminservice.user.dto;

import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class GetMemberResponseDto {

    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "회원 이름", example = "홍길동")
    private String name;

    @Schema(description = "회원 이메일", example = "이메일")
    private String email;

    @Schema(description = "회원 닉네임", example = "닉네임")
    private String nickname;

    @Schema(description = "회원 성별", example = "남성")
    private String gender;

    @Schema(description = "회원 유입 경로", example = "네이버")
    private String inflow;

    @Schema(description = "회원 생년월일", example = "1990-01-01")
    private LocalDate birthDate;

    @Schema(description = "회원 권한", example = "USER")
    private MemberRole role;

    @Schema(description = "회원 상태", example = "ACTIVE")
    private String state;

    @Schema(description = "생성일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일", example = "2021-07-01T00:00:00")
    private LocalDateTime updatedAt;

    @Builder
    private GetMemberResponseDto(Long id, String name, String email, String nickname, String gender, String inflow, LocalDate birthDate, MemberRole role, String state, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.inflow = inflow;
        this.birthDate = birthDate;
        this.role = role;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GetMemberResponseDto getMemberInfo(Member member) {
        return GetMemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .inflow(member.getInflow())
                .birthDate(member.getBirthDate())
                .role(member.getRole())
                .state(member.getState().toString())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}

package com.authservice.auth.dto;

import com.authservice.auth.entitiy.Member;
import com.authservice.auth.entitiy.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoDto {
    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "회원 이메일", example = "dyw1014@gachon.ac.kr")
    private String email;

    @Schema(description = "회원 이름", example = "홍길동")
    private String name;

    @Schema(description = "회원 비밀번호", example = "password")
    private String password;

    @Schema(description = "회원 권한", example = "ROLE_USER")
    private MemberRole role;

    @Builder
    public MemberInfoDto(
            Long id, String email, String name, String password, MemberRole role
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public static MemberInfoDto of(
            Long id, String email, String name, String password, MemberRole role
    ) {
        return MemberInfoDto.builder().id(id).email(email).name(name).password(password).role(role).build();
    }

    public static MemberInfoDto toDto(Member member) {
        return MemberInfoDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
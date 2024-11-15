package com.adminservice.user.entity;

import com.adminservice.global.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "member")
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema
public class Member extends BaseEntity {

    @Schema(description = "회원 이름", example = "홍길동")
    @Column(name="name", nullable = false)
    private String name;

    @Schema(description = "회원 이메일", example = "이메일")
    @Column(name = "email", nullable = false)
    private String email;

    @Schema(description = "회원 비밀번호", example = "비밀번호")
    @Column(name = "password", nullable = false)
    private String password;

    @Schema(description = "회원 생년월일", example = "1990-01-01")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Schema(description = "회원 유입 경로", example = "네이버")
    @Column(name = "inflow")
    private String inflow;

    @Schema(description = "회원 성별", example = "남성")
    @Column(name = "gender", nullable = false)
    private String gender;

    @Schema(description = "회원 닉네임", example = "닉네임")
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Schema(description = "회원 권한", example = "USER")
    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private MemberRole role;

    @Schema(description = "회원 상태", example = "ACTIVE")
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private MemberState state;

    @Schema(description = "마지막 로그인 시간", example = "2021-07-01")
    @Column(name = "last_login_at")
    private LocalDate lastLoginAt;

    @Builder
    public Member(
            String name, String email, String password, LocalDate birthDate,
            String inflow, String gender, String nickname, MemberRole role,
            MemberState state, LocalDate lastLoginAt
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.inflow = inflow;
        this.gender = gender;
        this.nickname = nickname;
        this.role = role;
        this.state = state;
        this.lastLoginAt = lastLoginAt;
    }

    public static Member of(
            String name, String email, String password, LocalDate birthDate,
            String inflow, String gender, String nickname, MemberRole role,
            MemberState state, LocalDate lastLoginAt
    ) {
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .birthDate(birthDate)
                .inflow(inflow)
                .gender(gender)
                .nickname(nickname)
                .role(role)
                .state(state)
                .lastLoginAt(lastLoginAt)
                .build();
    }


}



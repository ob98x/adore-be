package com.authservice.auth.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Table(name = "member")
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "inflow")
    private String inflow;

    @Column(name = "gender")
    private String gender;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "role")
    private MemberRole role;

    @Column(name = "state")
    private MemberState state;

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

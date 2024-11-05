package com.adminservice.penalty.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.report.entity.Report;
import com.adminservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "penalty")
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Penalty extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name="level", nullable = false)
    private PenaltyLevel level;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Builder
    public Penalty(PenaltyLevel level, Member member, Report report) {
        this.level = level;
        this.member = member;
        this.report = report;
    }

    public static Penalty of(PenaltyLevel level, Member member, Report report) {
        return Penalty.builder()
                .level(level)
                .member(member)
                .report(report)
                .build();
    }

}

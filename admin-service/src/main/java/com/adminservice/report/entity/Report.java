package com.adminservice.report.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report")
public class Report extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ReportCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_state", nullable = false)
    private ReportState state;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = false)
    private Member reportedBy;

    @OneToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Member target;

}
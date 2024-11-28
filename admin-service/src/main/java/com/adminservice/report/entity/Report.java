package com.adminservice.report.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.user.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report")
@Schema(name = "Report", description = "신고의 객체")
public class Report extends BaseEntity {

    @Schema(description = "신고 제목", example = "신고 제목")
    @Column(name = "title", nullable = false)
    private String title;

    @Schema(description = "신고 내용", example = "신고 내용")
    @Column(name = "content", nullable = false)
    private String content;

    @Schema(description = "신고 카테고리", example = "HATE")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ReportCategory category;

    @Schema(description = "신고 상태", example = "WAIT")
    @Enumerated(EnumType.STRING)
    @Column(name = "process_state", nullable = false)
    private ReportState state;

    @Schema(description = "신고 받은 리뷰 ID", example = "1")
    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Schema(description = "신고자 ID", example = "1")
    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = false)
    private Member reportedBy;

    @Schema(description = "신고 대상 ID", example = "1")
    @OneToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Member target;

    @Builder
    public Report(String title, String content, ReportCategory category, ReportState state, Long contentId, Member reportedBy, Member target) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.state = state;
        this.contentId = contentId;
        this.reportedBy = reportedBy;
        this.target = target;
    }

    public static Report of(String title, String content, ReportCategory category, ReportState state, Long contentId, Member reportedBy, Member target) {
        return Report.builder()
                .title(title)
                .content(content)
                .category(category)
                .state(state)
                .contentId(contentId)
                .reportedBy(reportedBy)
                .target(target)
                .build();
    }

}
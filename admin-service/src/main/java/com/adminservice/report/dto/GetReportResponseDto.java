package com.adminservice.report.dto;

import com.adminservice.report.entity.Report;
import com.adminservice.report.entity.ReportState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

public class GetReportResponseDto {

    @Schema(description = "신고 ID", example = "1")
    private Long id;

    @Schema(description = "신고 제목", example = "신고 제목")
    private String title;

    @Schema(description = "신고 내용", example = "신고 내용")
    private String content;

    @Schema(description = "신고자 ID", example = "1")
    private Long reporterId;

    @Schema(description = "신고자 이름", example = "신고자 이름")
    private String reporterName;

    @Schema(description = "신고자 이메일", example = "신고자 이메일")
    private String reporterEmail;

    @Schema(description = "신고 대상 ID", example = "1")
    private Long targetId;

    @Schema(description = "신고 대상 이름", example = "신고 대상 이름")
    private String targetName;

    @Schema(description = "신고 대상 이메일", example = "신고 대상 이메일")
    private String targetEmail;

    @Schema(description = "신고 카테고리", example = "HATE")
    private String category;

    @Schema(description = "신고 상태", example = "WAIT")
    private ReportState state;

    @Schema(description = "생성일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "수정일", example = "2021-07-01T00:00:00")
    private LocalDateTime updatedDate;

    @Schema(description = "처리 가능 여부", example = "true")
    private boolean canProcess;

    @Builder
    private GetReportResponseDto(Long id, String title, String content, Long reporterId, String reporterName, String reporterEmail, Long targetId, String targetName, String targetEmail, String category, ReportState state, LocalDateTime createdDate, LocalDateTime updatedDate, boolean canProcess) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.reporterEmail = reporterEmail;
        this.targetId = targetId;
        this.targetName = targetName;
        this.targetEmail = targetEmail;
        this.category = category;
        this.state = state;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.canProcess = canProcess;
    }

    public static GetReportResponseDto createResponse(Report report) {
        return GetReportResponseDto.builder()
                .id(report.getId())
                .title(report.getTitle())
                .content(report.getContent())
                .reporterId(report.getReportedBy().getId())
                .reporterName(report.getReportedBy().getNickname())
                .reporterEmail(report.getReportedBy().getEmail())
                .targetId(report.getTarget().getId())
                .targetName(report.getTarget().getName())
                .targetEmail(report.getTarget().getEmail())
                .category(report.getCategory().name())
                .state(report.getState())
                .createdDate(report.getCreatedAt())
                .updatedDate(report.getUpdatedAt())
                .canProcess(report.getState() == ReportState.WAIT)
                .build();
    }
}

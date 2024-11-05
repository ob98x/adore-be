package com.adminservice.report.dto;

import com.adminservice.question.entity.Question;
import com.adminservice.question.entity.QuestionState;
import com.adminservice.report.entity.Report;
import com.adminservice.report.entity.ReportState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

public class GetReportResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long reporterId;
    private String reporterName;
    private String reporterEmail;
    private Long targetId;
    private String targetName;
    private String targetEmail;
    private String category;
    private ReportState state;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
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

package com.adminservice.report.dto;

import com.adminservice.report.entity.Report;
import com.adminservice.report.entity.ReportCategory;
import com.adminservice.report.entity.ReportState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetReportListResponseDto {

    @Schema(description = "신고 목록", example = "[{\"title\": \"신고 제목\", \"nickname\": \"신고자 닉네임\", \"email\": \"신고자 이메일\", \"state\": \"WAITING\", \"createdAt\": \"2021-07-01T00:00:00\"}]")
    private List<ReportListInfo> reportList;

    @Schema(description = "총 페이지 수", example = "1")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "false")
    private boolean hasNext;

    @Getter
    @Setter
    public static class ReportListInfo {

        @Schema(description = "id", example = "1")
        private Long id;

        @Schema(description = "신고 제목", example = "신고 제목")
        private String title;

        @Schema(description = "신고자 닉네임", example = "신고자 닉네임")
        private String nickname;

        @Schema(description = "신고자 이메일", example = "신고자 이메일")
        private String email;

        @Schema(description = "신고 카테고리", example = "HATE")
        private ReportCategory category;

        @Schema(description = "신고 상태", example = "WAITING")
        private ReportState state;

        @Schema(description = "생성일", example = "2021-07-01T00:00:00")
        private LocalDateTime createdAt;

        // 정적 팩토리 메서드를 추가하여 변환 간소화
        public static ReportListInfo fromReport(Report report) {
            ReportListInfo info = new ReportListInfo();
            info.setId(report.getId());
            info.setTitle(report.getTitle());
            info.setNickname(report.getReportedBy().getNickname());
            info.setEmail(report.getReportedBy().getEmail());
            info.setState(report.getState());
            info.setCategory(report.getCategory());
            info.setCreatedAt(report.getCreatedAt());
            return info;
        }
    }
    public static GetReportListResponseDto createResponse(List<ReportListInfo> reportList, int totalPages, boolean hasNext) {
        GetReportListResponseDto response = new GetReportListResponseDto();
        response.setReportList(reportList);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }
}

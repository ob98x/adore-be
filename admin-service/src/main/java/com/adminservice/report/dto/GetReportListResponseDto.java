package com.adminservice.report.dto;

import com.adminservice.report.entity.Report;
import com.adminservice.report.entity.ReportCategory;
import com.adminservice.report.entity.ReportState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetReportListResponseDto {

    private List<ReportListInfo> reportList;
    private int totalPages;
    private boolean hasNext;

    @Getter
    @Setter
    public static class ReportListInfo {
        private String title;
        private String nickname;
        private String email;
        private ReportCategory category;
        private ReportState state;
        private LocalDateTime createdAt;

        // 정적 팩토리 메서드를 추가하여 변환 간소화
        public static ReportListInfo fromReport(Report report) {
            ReportListInfo info = new ReportListInfo();
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

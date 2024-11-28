package com.adminservice.report.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.report.dto.GetReportListResponseDto;
import com.adminservice.report.dto.GetReportResponseDto;
import com.adminservice.report.entity.ReportCategory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReportService {
    ResponseEntity<GetReportResponseDto> getReport(Long id);
    GetReportListResponseDto getReportLists(FilterType filterType, ReportCategory category, int page);
    ResponseEntity<CustomResponseCode> processReport(Long id, String penalty);
    Long createReport(Long contentId, String title, String category, Long targetId, String content, Long reporterId);
}

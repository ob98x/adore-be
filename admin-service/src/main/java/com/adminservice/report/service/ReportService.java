package com.adminservice.report.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.report.dto.GetReportListResponseDto;
import com.adminservice.report.dto.GetReportResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReportService {
    ResponseEntity<GetReportResponseDto> getReport(Long id);
    GetReportListResponseDto getReportLists(FilterType filterType, int page);
    ResponseEntity<CustomResponseCode> processReport(Long id, String penalty);
    List<GetReportListResponseDto.ReportListInfo> allReports();
}

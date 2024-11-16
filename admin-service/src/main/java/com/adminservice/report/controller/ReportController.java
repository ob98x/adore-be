package com.adminservice.report.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.report.dto.GetReportListResponseDto;
import com.adminservice.report.dto.GetReportResponseDto;
import com.adminservice.report.entity.ReportCategory;
import com.adminservice.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[관리자] 신고 관련 API", description = "Report API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/report")
@Slf4j
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "신고 사항 리스트 검색 API", description = "신고 사항 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetReportListResponseDto> getReportLists(
            @RequestParam(value = "filter") FilterType filterType,
            @RequestParam(value = "category") ReportCategory category,
            @PathVariable("page") int page) {
        log.info("[Report Controller - getReportLists]: 신고사항 리스트 조회 요청이 들어왔습니다. page: {}, filter: {}", page, filterType);
        GetReportListResponseDto response = reportService.getReportLists(filterType, category, page-1);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "신고 사항 조회 API", description = "신고 사항을 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetReportResponseDto> viewMemberInfo(@Parameter(description = "조회할 신고사항 id") @RequestParam Long id) {
        log.info("[Report Controller - viewMemberInfo]: {}번 신고사항 조회 요청이 들어왔습니다.", id);
        return reportService.getReport(id);
    }

    @Operation(summary = "신고 사항 삭제 API", description = "신고 사항을 삭제합니다.")
    @PostMapping("/process")
    public ResponseEntity<CustomResponseCode> processReport(@Parameter(description = "처리할 신고 사항 id") @RequestParam Long id, @RequestBody String answerContent) {
        log.info("[Report Controller - processReport]: {}번 신고사항 삭제 요청이 들어왔습니다.", id);
        return reportService.processReport(id, answerContent);
    }
}

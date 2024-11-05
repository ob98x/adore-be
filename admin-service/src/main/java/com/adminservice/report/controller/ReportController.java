package com.adminservice.report.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.report.dto.GetReportListResponseDto;
import com.adminservice.report.dto.GetReportResponseDto;
import com.adminservice.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/report")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/lists/{page}")
    public ResponseEntity<GetReportListResponseDto> getReportLists(
            @RequestParam("filter") FilterType filterType,
            @PathVariable("page") int page) {
        GetReportListResponseDto response = reportService.getReportLists(filterType,page-1);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/")
    public ResponseEntity<GetReportResponseDto> viewMemberInfo(@RequestParam Long id) {
        return reportService.getReport(id);
    }

    @DeleteMapping("/process")
    public ResponseEntity<CustomResponseCode> deleteMember(@RequestParam Long id, @RequestBody String answerContent) {
        return reportService.processReport(id, answerContent);
    }
}

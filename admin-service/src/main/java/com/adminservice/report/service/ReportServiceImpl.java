package com.adminservice.report.service;

import com.adminservice.global.CustomException;
import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.global.ResponseCode;
import com.adminservice.penalty.entity.Penalty;
import com.adminservice.penalty.entity.PenaltyLevel;
import com.adminservice.penalty.repository.PenaltyRepository;
import com.adminservice.report.dto.GetReportListResponseDto;
import com.adminservice.report.dto.GetReportResponseDto;
import com.adminservice.report.entity.Report;
import com.adminservice.report.entity.ReportState;
import com.adminservice.report.repository.ReportRepository;
import com.adminservice.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PenaltyRepository penaltyRepository;

    @Override
    public ResponseEntity<GetReportResponseDto> getReport(Long id) {
        return ResponseEntity.ok(GetReportResponseDto.createResponse(checkConflictReport(id)));
    }


    @Override
    public ResponseEntity<CustomResponseCode> processReport(Long id, String penaltyLevel) {
        Report report = checkConflictReport(id);
        Member target = report.getTarget();

        report.setState(ReportState.COMPLETE);

        penaltyRepository.save(Penalty.of(PenaltyLevel.valueOf(penaltyLevel), target, report));
        reportRepository.save(report);
        return ResponseEntity.ok(CustomResponseCode.REPORT_PROCESS_SUCCESS);
    }

    @Override
    public List<GetReportListResponseDto.ReportListInfo> allReports() {
        List<Report> reportList = reportRepository.findAll();
        return reportList.stream()
                .map(GetReportListResponseDto.ReportListInfo::fromReport)
                .toList();
    }

    @Override
    public GetReportListResponseDto getReportLists(FilterType filterType, int page) {
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.=

        Specification<Report> spec = Specification.where(null);

        if (filterType == FilterType.WAIT) {
            spec = spec.and( (root, query, cb) ->
                    cb.equal(root.get("state"), ReportState.WAIT));
        } else if (filterType == FilterType.COMPLETE) {
            spec = spec.and( (root, query, cb) ->
                    cb.equal(root.get("state"), ReportState.COMPLETE));
        } else {
            spec = spec.and( (root, query, cb) ->
                    cb.notEqual(root.get("state"), ReportState.INACTIVE));
        }

        Page<Report> resultPage = reportRepository.findAll(spec, pageable);
        List<GetReportListResponseDto.ReportListInfo> reportList = resultPage.getContent().stream()
                .map(GetReportListResponseDto.ReportListInfo::fromReport)
                .toList();

        return GetReportListResponseDto.createResponse(reportList, resultPage.getTotalPages(), resultPage.hasNext());

    }

    public Report checkConflictReport(Long id) {
        // Check if the question exists
        if (reportRepository.findReportById(id).isEmpty()) {
            throw new CustomException(ResponseCode.REPORT_NOT_FOUND);
        }

        // Check if the question is inactive
        if (reportRepository.findReportById(id).get().getState().equals(ReportState.INACTIVE)) {
            throw new CustomException(ResponseCode.REPORT_DELETED);
        }

        return reportRepository.findReportById(id).get();
}}

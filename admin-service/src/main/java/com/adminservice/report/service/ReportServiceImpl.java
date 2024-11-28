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
import com.adminservice.report.entity.ReportCategory;
import com.adminservice.report.entity.ReportState;
import com.adminservice.report.repository.ReportRepository;
import com.adminservice.user.entity.Member;
import com.adminservice.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PenaltyRepository penaltyRepository;
    private final MemberService memberService;

    @Override
    public ResponseEntity<GetReportResponseDto> getReport(Long id) {
        log.info("[Report Service - getReport]: {}번 신고사항 조회 요청이 들어왔습니다.", id);
        return ResponseEntity.ok(GetReportResponseDto.createResponse(checkConflictReport(id)));
    }


    @Override
    public ResponseEntity<CustomResponseCode> processReport(Long id, PenaltyLevel penaltyLevel) {
        log.info("[Report Service - processReport]: {}번 신고사항 처리 요청이 들어왔습니다.", id);
        Report report = checkConflictReport(id);
        Member target = report.getTarget();


        report.setState(ReportState.COMPLETE);

        if (penaltyLevel.toString().equals("NONE")) {
            log.info("[Report Service - processReport]: 신고사항 처리를 완료했습니다. id: {}", id);
            report.setState(ReportState.INACTIVE);
            reportRepository.save(report);
            return ResponseEntity.ok(CustomResponseCode.REPORT_PROCESS_SUCCESS);
        }

        log.info("[Report Service - processReport]: 신고사항 처리를 완료했습니다. id: {}", id);
        penaltyRepository.save(Penalty.of(penaltyLevel, target, report));
        reportRepository.save(report);
        return ResponseEntity.ok(CustomResponseCode.REPORT_PROCESS_SUCCESS);
    }

    @Override
    public GetReportListResponseDto getReportLists(FilterType filterType, ReportCategory category, int page) {
        log.info("[Report Service - getReportLists]: 신고사항 리스트 조회 요청이 들어왔습니다. page: {}, filter: {}", page, filterType);
        Pageable pageable = PageRequest.of(page, 10);

        log.info("[Report Service - getReportLists]: 검색 조건을 설정합니다.");
        Specification<Report> spec = Specification.where(null);

        if (filterType == FilterType.WAIT) {
            spec = spec.and( (root, query, cb) ->
                    cb.equal(root.get("state"), ReportState.WAIT));
        } else {
            spec = spec.and( (root, query, cb) ->
                    cb.equal(root.get("state"), ReportState.COMPLETE));
        }

        if (category == ReportCategory.REVIEW) {
            spec = spec.and( (root, query, cb) ->
                    cb.equal(root.get("category"), ReportCategory.REVIEW));
        } else if (category == ReportCategory.COMMENT) {
            spec = spec.and( (root, query, cb) ->
                    cb.equal(root.get("category"), ReportCategory.COMMENT));
        }

        log.info("[Report Service - getReportLists]: 신고사항 리스트를 조회합니다.");
        Page<Report> resultPage = reportRepository.findAll(spec, pageable);

        log.info("[Report Service - getReportLists]: 신고사항 리스트를 DTO 로 변환합니다.");
        List<GetReportListResponseDto.ReportListInfo> reportList = resultPage.getContent().stream()
                .map(GetReportListResponseDto.ReportListInfo::fromReport)
                .toList();

        return GetReportListResponseDto.createResponse(reportList, resultPage.getTotalPages(), resultPage.hasNext());

    }

    @Override
    @Transactional
    public Long createReport(Long contentId, String title, String category, Long targetId, String content, Long reporterId) {
        log.info("[Report Service - createReport]: 신고사항 생성 요청이 들어왔습니다. contentId: {}, title: {}, category: {}, targetId: {}, content: {}, reporterId: {}",contentId, title, category, targetId, content, reporterId);

        log.info("[Report Service - createReport]: 신고 대상과 신고자 정보를 조회합니다.");
        Member target = memberService.checkConflictMember(targetId);
        Member reporter = memberService.checkConflictMember(reporterId);

        log.info("[Report Service - createReport]: 신고사항을 생성합니다.");
        Report report = Report.builder()
                .title(title)
                .content(content)
                .category(ReportCategory.valueOf(category))
                .state(ReportState.WAIT)
                .contentId(contentId)
                .reportedBy(reporter)
                .target(target)
                .build();

        reportRepository.save(report);
        return report.getId();
    }

    public Report checkConflictReport(Long id) {
        log.info("[Report Service - checkConflictReport]: 신고사항 정보를 조회합니다. id: {}", id);
        if (reportRepository.findReportById(id).isEmpty()) {
            log.error("[Report Service - checkConflictReport]: 신고사항 정보를 찾을 수 없습니다. id: {}", id);
            throw new CustomException(ResponseCode.REPORT_NOT_FOUND);
        }
        if (reportRepository.findReportById(id).get().getState().equals(ReportState.INACTIVE)) {
            log.error("[Report Service - checkConflictReport]: 삭제된 신고사항 정보입니다. id: {}", id);
            throw new CustomException(ResponseCode.REPORT_DELETED);
        }

        return reportRepository.findReportById(id).get();
}

}

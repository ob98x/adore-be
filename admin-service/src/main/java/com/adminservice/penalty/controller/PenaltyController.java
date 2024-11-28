package com.adminservice.penalty.controller;

import com.adminservice.global.FilterType;
import com.adminservice.penalty.entity.PenaltyLevel;
import com.adminservice.penalty.service.PenaltyService;
import com.adminservice.report.dto.GetPenaltyListResponseDto;
import com.adminservice.report.dto.GetReportListResponseDto;
import com.adminservice.report.entity.ReportCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[관리자] 페널티 관련 API", description = "Penalty API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/penalty")
@Slf4j
public class PenaltyController {

    private final PenaltyService penaltyService;

    @Operation(summary = "페널티 확인 API", description = "페널티를 확인합니다.")
    @GetMapping("/check")
    public boolean checkPenalty(
            @RequestParam @Parameter(description = "페널티를 확인할 회원 id") Long memberId) {
        log.info("[ admin service - Penalty Controller ]: 페널티 확인 요청이 들어왔습니다.");
        return penaltyService.checkPenalty(memberId);
    }

    @Operation(summary = "페널티 회원 확인 API", description = "페널티 회원을 확인합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetPenaltyListResponseDto> getPenaltyMembers(
            @RequestParam(value = "penaltyLevel") PenaltyLevel penaltyLevel,
            @PathVariable("page") int page) {
        log.info("[ admin service - Penalty Controller ]: 페널티 회원 조회 요청이 들어왔습니다. page: {}", page);
        GetPenaltyListResponseDto response = penaltyService.getPenaltyMembers(penaltyLevel, page-1);
        return ResponseEntity.ok(response);
    }

}

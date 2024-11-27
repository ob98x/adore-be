package com.adminservice.penalty.controller;

import com.adminservice.penalty.service.PenaltyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

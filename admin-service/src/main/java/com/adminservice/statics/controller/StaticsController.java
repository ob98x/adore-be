package com.adminservice.statics.controller;

import com.adminservice.statics.dto.DateCountDto;
import com.adminservice.statics.service.StaticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "[관리자] 통계 관련 API", description = "Statics API")
@RestController
    @RequestMapping("/api/admin/statics")
@RequiredArgsConstructor
@Slf4j
public class StaticsController {
    private final StaticsService staticsService;

    @Operation(summary = "신규 사용자 통계 조회 API", description = "신규 사용자 통계를 조회합니다.")
    @GetMapping("/newUser")
    public ResponseEntity<DateCountDto> getNewUserStatics (
            @Parameter(description = "시작일") @RequestParam LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam LocalDate endDate) {
        log.info("[Statics Controller - getNewUserStatics]: 신규 사용자 통계 조회 요청이 들어왔습니다. 시작일: {}, 종료일: {}", startDate, endDate);
        return ResponseEntity.ok(staticsService.getNewUserStatics(startDate, endDate));
    }


    @Operation(summary = "미접속 사용자 통계 조회 API", description = "미접속 사용자 통계를 조회합니다.")
    @GetMapping("/inactiveMembers")
    public  ResponseEntity<DateCountDto> getInactiveMembers(
            @Parameter(description = "시작일") @RequestParam LocalDate startDate) {
        log.info("[Statics Controller - getInactiveMembers]: 미접속 사용자 통계 조회 요청이 들어왔습니다.");
        return ResponseEntity.ok(staticsService.getInactiveMembers(startDate));
    }

    @Operation(summary = "접속 사용자 통계 조회 API", description = "접속 사용자 통계를 조회합니다.")
    @GetMapping("/activeUser")
    public ResponseEntity<DateCountDto> getActiveUserStatics(
            @Parameter(description = "시작일") @RequestParam LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam LocalDate endDate) {
        log.info("[Statics Controller - getActiveUserStatics]: 접속 사용자 통계 조회 요청이 들어왔습니다. 시작일: {}, 종료일: {}", startDate, endDate);
        return ResponseEntity.ok(staticsService.getActiveMembers(startDate, endDate));
    }

    @Operation(summary = "추천 기능 이용자 통계 조회 API", description = "추천 기능 이용자 통계를 조회합니다.")
    @GetMapping("/recommendUser")
    public ResponseEntity<DateCountDto> getRecommendUserStatics(
            @Parameter(description = "시작일") @RequestParam LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam LocalDate endDate) {
        log.info("[Statics Controller - getRecommendUserStatics]: 추천 기능 이용자 통계 조회 요청이 들어왔습니다. 시작일: {}, 종료일: {}", startDate, endDate);
        return ResponseEntity.ok(staticsService.getRecommendUser(startDate, endDate));
    }
}

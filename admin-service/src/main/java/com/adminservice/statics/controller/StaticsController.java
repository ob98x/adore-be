package com.adminservice.statics.controller;

import com.adminservice.statics.dto.GetStaticsResponseDto;
import com.adminservice.statics.service.StaticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "[관리자] 통계 관련 API", description = "Statics API")
@RestController
@RequestMapping("/admin/statics")
@RequiredArgsConstructor
public class StaticsController {
    private final StaticsService staticsService;

    @Operation(summary = "신규 사용자 통계 조회 API", description = "신규 사용자 통계를 조회합니다.")
    @GetMapping("/newUser")
    public ResponseEntity<GetStaticsResponseDto> getNewUserStatics (
            @Parameter(description = "시작일") @RequestParam LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(staticsService.getNewUserStatics(startDate, endDate));
    }


    @Operation(summary = "미접속 사용자 통계 조회 API", description = "미접속 사용자 통계를 조회합니다.")
    @GetMapping("/inactiveMembers")
    public  ResponseEntity<GetStaticsResponseDto> getInactiveMembers() {
        return ResponseEntity.ok(staticsService.getInactiveMembers());
    }

    @Operation(summary = "접속 사용자 통계 조회 API", description = "접속 사용자 통계를 조회합니다.")
    @GetMapping("/activeUser")
    public ResponseEntity<GetStaticsResponseDto> getActiveUserStatics(
            @Parameter(description = "시작일") @RequestParam LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(staticsService.getActiveMembers(startDate, endDate));
    }

    @Operation(summary = "추천 기능 이용자 통계 조회 API", description = "추천 기능 이용자 통계를 조회합니다.")
    @GetMapping("/recommendUser")
    public ResponseEntity<GetStaticsResponseDto> getRecommendUserStatics(
            @Parameter(description = "시작일") @RequestParam LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(staticsService.getRecommendUser(startDate, endDate));
    }
}

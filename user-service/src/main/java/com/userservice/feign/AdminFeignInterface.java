package com.userservice.feign;

import com.userservice.global.CustomResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "admin-service", path = "/api/admin")
public interface AdminFeignInterface {

    @GetMapping("/penalty/check")
    boolean checkPenalty(@RequestParam @Parameter(description = "페널티를 확인할 회원 id") Long memberId);

    @Operation(summary = "문의 사항 생성 API", description = "문의 사항을 생성합니다.")
    @PostMapping("/question/create")
    Long createQuestion(
            @RequestParam @Parameter(description = "문의 제목") String title,
            @RequestParam @Parameter(description = "문의 내용") String content,
            @RequestParam @Parameter(description = "문의 카테고리") String category,
            @RequestParam @Parameter(description = "문의자 id") Long memberId);

    @Operation(summary = "신고 사항 생성 API", description = "신고 사항을 생성합니다.")
    @PostMapping("/report/create")
    Long createReport(
            @RequestParam("category") String category,
            @RequestParam("targetId") Long targetId,
            @RequestParam("content") String content,
            @RequestParam("contentId") Long contentId,
            @RequestParam("reporterId") Long reporterId,
            @RequestParam("title") String title);
}

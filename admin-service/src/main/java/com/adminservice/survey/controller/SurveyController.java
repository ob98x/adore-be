package com.adminservice.survey.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.global.SearchType;
import com.adminservice.survey.dto.GetSurveyListResponseDto;
import com.adminservice.survey.dto.GetSurveyResponseDto;
import com.adminservice.survey.dto.SurveyCreateRequestDto;
import com.adminservice.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[관리자] 관리자 설문 관련 API", description = "Admin/survey")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/survey")
public class SurveyController {
    private final SurveyService surveyService;

    // 1. 설문 생성 API
    @PostMapping("/create")
    public ResponseEntity<CustomResponseCode> createSurvey(
            @RequestBody SurveyCreateRequestDto dto,
            @RequestHeader("authorization") String authorization
            ){
        return surveyService.createSurvey(dto, authorization);
    }

    // 2. 설문 수정 API -> 사실상 질문 테이블이랑 답변 테이블은 생성에 가까운데 survey 테이블은 안바뀜
    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updateSurvey(
            @RequestBody SurveyCreateRequestDto dto, // TODO : 이건 다른 걸 써야할지도?
            @RequestParam Long surveyId
    ){
        return surveyService.updateSurvey(dto, surveyId);
    }

    // 3. 설문 삭제 API
    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteSurvey(
            @RequestParam Long surveyId
    ){
        return surveyService.deleteSurvey(surveyId);
    }

    // 4. 설문 목록 조회 API
    @GetMapping("/list/{page}")
    public ResponseEntity<GetSurveyListResponseDto> getSurveyList(
            @PathVariable("page") int page,
            @RequestParam("filter") FilterType filterType
    ) {
        return ResponseEntity.ok(surveyService.getSurveyList(filterType, page-1));
    }

    // 5. 설문 세부 조회 API
    @GetMapping("/")
    public ResponseEntity<GetSurveyResponseDto> getSurveyInfo(@RequestParam Long surveyId) {
        return ResponseEntity.ok(surveyService.getSurveyInfo(surveyId));
    }
}

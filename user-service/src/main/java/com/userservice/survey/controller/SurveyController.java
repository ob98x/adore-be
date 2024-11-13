package com.userservice.survey.controller;

import com.userservice.global.CustomResponseCode;
import com.userservice.survey.dto.*;
import com.userservice.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/recomm")
public class SurveyController {

    private final SurveyService surveyService;

    // 1. 1번 질문 전달
    @GetMapping("/first-question")
    ResponseEntity<GetQuestionsDto> getFirstQuestions() {
        return ResponseEntity.ok(surveyService.getFirstQuestions());
    }

    // 2. 1번 질문에 대해서 전달받아서 새로운 질문 전달
    @GetMapping("/questions/{surveyId}")
    ResponseEntity<GetQuestionsDto> getAdditionalQuestions(
            @PathVariable("surveyId") Long surveyId,
            @RequestParam("nxt1") Long nxtQstId1,
            @RequestParam("nxt2") Long nxtQstId2,
            @RequestParam("nxt3") Long nxtQstId3 ) {
        List<Long> nxtQstIds = List.of(nxtQstId1,nxtQstId2,nxtQstId3);

        return ResponseEntity.ok(surveyService.getAdditionalQuestions(surveyId, nxtQstIds)); // 서비스가 들어와야 함
    }

    // 3. 설문 결과 저장 및 fastapi로 추천 요청
    @PostMapping("/result")
    ResponseEntity<GetRecommendPerfumes> postSurveyResult(
            @RequestBody RequestSurveyResultDto dto
    ) {
        return ResponseEntity.ok(surveyService.saveSurveyResultAndGetRecommendPerfume(dto));
    }

    // 4. 만족도 결과 저장
    @PostMapping("/satisfaction")
    ResponseEntity<CustomResponseCode> postSatisfactionSurvey(
            @RequestBody SatisfactionResultDto dto
    ){
        surveyService.saveSatisfactionResult(dto);
        return ResponseEntity.ok(CustomResponseCode.SATISFACTION_CREATE_SUCCESS);
    }

    // 5. 추천 결과 조회
    @GetMapping("/result/{memberId}")
    ResponseEntity<GetRecommendPerfumes> getSurveyResult(
            @PathVariable("memberId") Long memberId
    ){
        return ResponseEntity.ok(surveyService.getRecommendPerfumeResult(memberId));
    }

    // 6. 친구 추천 요청
    @PostMapping("/friend/result")
    ResponseEntity<GetRecommendPerfumes> postFriendSurveyResult(
            @RequestBody RequestFriendSurveyResultDto dto
    ) {
        return ResponseEntity.ok(surveyService.saveFriendSurveyResultAndGetRecommendPerfume(dto));
    }

    // 7. 친구 추천 결과 조회
    @GetMapping("/friend/result/{memberId}")
    ResponseEntity<GetRecommendPerfumes> getFriendSurveyResult(
            @PathVariable("memberId") Long memberId
    ){
        return ResponseEntity.ok(surveyService.getFriendRecommendPerfumeResult(memberId));
    }
}

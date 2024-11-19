package com.userservice.survey.controller;

import com.userservice.global.CustomResponseCode;
import com.userservice.global.SearchType;
import com.userservice.survey.dto.*;
import com.userservice.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[사용자] 설문 관련 API", description = "Survey API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user/recomm")
public class SurveyController {

    private final SurveyService surveyService;

    // 1. 1번 질문 전달
    @Operation(summary = "첫 번째 질문 조회 API", description = "첫 번째 질문을 조회합니다.")
    @GetMapping("/first-question")
    ResponseEntity<GetQuestionsDto> getFirstQuestions() {
        return ResponseEntity.ok(surveyService.getFirstQuestions());
    }

    // 2. 1번 질문에 대해서 전달받아서 새로운 질문 전달
    @Operation(summary = "나머지 질문들 조회 API", description = "첫 번째 질문의 답변에 연결된 나머지 질문들을 조회합니다.")
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
    @Operation(summary = "설문 결과 저장 및 추천 요청 API", description = "설문 결과를 저장하고 FAST API 서버로 추천을 요청합니다.")
    @PostMapping("/result")
    ResponseEntity<GetRecommendPerfumes> postSurveyResult(
            @RequestBody RequestSurveyResultDto dto,
            @RequestHeader("Authorization") String authorization
    ) {
        return ResponseEntity.ok(surveyService.saveSurveyResultAndGetRecommendPerfume(dto, authorization));
    }

    // 4. 만족도 결과 저장
    @Operation(summary = "만족도 결과 저장 API", description = "설문에 대한 만족도 조사 결과를 저장합니다.")
    @PostMapping("/satisfaction")
    ResponseEntity<CustomResponseCode> postSatisfactionSurvey(
            @RequestBody SatisfactionResultDto dto
    ){
        surveyService.saveSatisfactionResult(dto);
        return ResponseEntity.ok(CustomResponseCode.SATISFACTION_CREATE_SUCCESS);
    }

    // 5. 추천 결과 세부 조회
    @Operation(summary = "추천 결과 세부 조회 API", description = "설문을 통해 추천받은 향수들을 조회합니다.")
    @GetMapping("/result/{userAnsId}")
    ResponseEntity<GetRecommendPerfumes> getSurveyResult(
            @PathVariable("userAnsId") Long userAnsId
    ){
        return ResponseEntity.ok(surveyService.getRecommendPerfumeResult(userAnsId));
    }

    // 6. 친구 추천 요청
    @Operation(summary = "친구 정보 저장 및 추천 요청 API", description = "친구 정보를 저장하고 FAST API 서버로 추천을 요청합니다.")
    @PostMapping("/friend")
    ResponseEntity<GetRecommendPerfumes> postFriendSurveyResult(
            @RequestBody RequestFriendSurveyResultDto dto,
            @RequestHeader("Authorization") String authorization
    ) {
        return ResponseEntity.ok(surveyService.saveFriendSurveyResultAndGetRecommendPerfume(dto, authorization));
    }

    // 7. 친구 추천 결과 조회
    @Operation(summary = "친구 추천 결과 세부 조회 API", description = "친구 정보를 통해 추천받은 향수들을 조회합니다.")
    @GetMapping("/friend/{friendId}")
    ResponseEntity<GetRecommendPerfumes> getFriendSurveyResult(
            @PathVariable("friendId") Long friendId
    ){
        return ResponseEntity.ok(surveyService.getFriendRecommendPerfumeResult(friendId));
    }

    // 8. 추천 결과 목록 조회
    @Operation(summary = "추천 결과 목록 조회 API", description = "설문을 통해 추천받은 결과 목록을 조회합니다.")
    @GetMapping("/result/list/{page}")
    ResponseEntity<GetSurveyResultListResponseDto> getSurveyResultList(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType, // 여기서 제공할 검색은 없음 뺄 수도 있음
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestHeader("Authorization") String authorization
    ){
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = ""; // 빈 문자열 또는 서비스 로직에서 null을 처리
        }
        return ResponseEntity.ok(surveyService.getSurveyResultList(searchType, keyword, page-1, authorization));
    }

    // 9. 친구 추천 결과 목록 조회
    @Operation(summary = "친구 추천 결과 목록 조회 API", description = "친구 정보를 통해 추천받은 결과 목록을 조회합니다.")
    @GetMapping("/friend/list/{page}")
    ResponseEntity<GetFriendResultListResponseDto> getFriendSurveyResultList(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType, // 친구 이름이라 이런 것들은 검색할만할지도
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestHeader("Authorization") String authorization
    ){
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = ""; // 빈 문자열 또는 서비스 로직에서 null을 처리
        }
        return ResponseEntity.ok(surveyService.getFriendResultList(searchType, keyword, page-1, authorization));
    }

    // 10. 본인 설문 삭제
    @Operation(summary = "본인 설문 삭제 API", description = "본인이 작성한 설문을 삭제합니다.")
    @DeleteMapping("/result/{userAnsId}")
    ResponseEntity<CustomResponseCode> deleteUserAns(
            @PathVariable("userAnsId") Long userAnsId,
            @RequestHeader("Authorization") String authorization
    ){
        return surveyService.deleteUserAns(userAnsId, authorization);
    }

    // 11. 친구 설문 삭제
    @Operation(summary = "친구 설문 삭제 API", description = "친구 설문을 삭제합니다.")
    @DeleteMapping("/friend/{friendId}")
    ResponseEntity<CustomResponseCode> deleteFriend(
            @PathVariable("friendId") Long friendId,
            @RequestHeader("Authorization") String authorization
    ){
        return surveyService.deleteFriend(friendId, authorization);
    }
}

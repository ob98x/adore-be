package com.adminservice.question.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.global.SearchType;
import com.adminservice.question.dto.GetQuestionListResponseDto;
import com.adminservice.question.dto.GetQuestionResponseDto;
import com.adminservice.question.entity.QuestionCategory;
import com.adminservice.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[관리자] 문의 관련 API", description = "Question API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/question")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "[미사용] 문의 사항 리스트 검색 API", description = "문의 사항 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetQuestionListResponseDto> searchQuestions(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("filter") FilterType filterType,
            @RequestParam("category") QuestionCategory category,
            @RequestParam("keyword") String searchKeyword) {
        GetQuestionListResponseDto response = questionService.getQuestionList(searchType, filterType, category, searchKeyword, page-1);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "문의 사항 리스트 조회 API", description = "문의 사항 리스트를 조회합니다.")
    @GetMapping("/list/")
    public ResponseEntity<List<GetQuestionListResponseDto.QuestionListInfo>> searchQuestions() {
        List<GetQuestionListResponseDto.QuestionListInfo> response = questionService.allQuestions();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "문의 사항 조회 API", description = "문의 사항을 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetQuestionResponseDto> viewMemberInfo(@Parameter(description = "조회할 문의사항 id") @RequestParam Long id) {
        return questionService.getQuestions(id);
    }

    @Operation(summary = "문의 사항 삭제 API", description = "문의 사항을 삭제합니다.")
    @DeleteMapping("/process")
    public ResponseEntity<CustomResponseCode> deleteMember(@Parameter(description = "삭제할 문의사항 id") @RequestParam Long id, @RequestBody String answerContent) {
        return questionService.processQuestions(id, answerContent);
    }
}

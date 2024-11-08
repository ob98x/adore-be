package com.adminservice.question.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.global.SearchType;
import com.adminservice.question.dto.GetQuestionListResponseDto;
import com.adminservice.question.dto.GetQuestionResponseDto;
import com.adminservice.question.entity.QuestionCategory;
import com.adminservice.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/question")
public class QuestionController {

    private final QuestionService questionService;

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


    @GetMapping("/")
    public ResponseEntity<GetQuestionResponseDto> viewMemberInfo(@RequestParam Long id) {
        return questionService.getQuestions(id);
    }

    @DeleteMapping("/process")
    public ResponseEntity<CustomResponseCode> deleteMember(@RequestParam Long id, @RequestBody String answerContent) {
        return questionService.processQuestions(id, answerContent);
    }
}

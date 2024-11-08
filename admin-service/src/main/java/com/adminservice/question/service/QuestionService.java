package com.adminservice.question.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.global.SearchType;
import com.adminservice.question.dto.GetQuestionListResponseDto;
import com.adminservice.question.dto.GetQuestionResponseDto;
import com.adminservice.question.entity.QuestionCategory;
import org.springframework.http.ResponseEntity;

public interface QuestionService {
    ResponseEntity<GetQuestionResponseDto> getQuestions(Long id);
    ResponseEntity<CustomResponseCode> processQuestions(Long id, String answerContent);
    GetQuestionListResponseDto getQuestionList(SearchType searchType, FilterType filterType, QuestionCategory category, String searchKeyword, int page);

}

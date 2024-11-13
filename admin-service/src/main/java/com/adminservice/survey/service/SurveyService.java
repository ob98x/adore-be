package com.adminservice.survey.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.survey.dto.GetSurveyListResponseDto;
import com.adminservice.survey.dto.GetSurveyResponseDto;
import com.adminservice.survey.dto.SurveyCreateRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SurveyService {
    ResponseEntity<CustomResponseCode> createSurvey(SurveyCreateRequestDto dto);

    ResponseEntity<CustomResponseCode> updateSurvey(SurveyCreateRequestDto dto, Long surveyId);

    ResponseEntity<CustomResponseCode> deleteSurvey(Long surveyId);

    GetSurveyResponseDto getSurveyInfo(Long surveyId);

    List<GetSurveyListResponseDto.SurveyListInfo> getSurveyList();
}

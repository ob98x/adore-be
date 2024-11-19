package com.userservice.survey.service;

import com.userservice.global.CustomResponseCode;
import com.userservice.global.SearchType;
import com.userservice.survey.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SurveyService {

    GetQuestionsDto getAdditionalQuestions(Long surveyId, List<Long> nxtQstIds);

    GetQuestionsDto getFirstQuestions();

    GetRecommendPerfumes saveSurveyResultAndGetRecommendPerfume(RequestSurveyResultDto dto, String authorization);

    void saveSatisfactionResult(SatisfactionResultDto dto);

    GetRecommendPerfumes getRecommendPerfumeResult(Long userAnsId);

    GetRecommendPerfumes saveFriendSurveyResultAndGetRecommendPerfume(RequestFriendSurveyResultDto dto, String authorization);

    GetRecommendPerfumes getFriendRecommendPerfumeResult(Long friendId);

    GetSurveyResultListResponseDto getSurveyResultList(SearchType searchType, String keyword, int page, String authorization);

    GetFriendResultListResponseDto getFriendResultList(SearchType searchType, String keyword, int page, String authorization);

    ResponseEntity<CustomResponseCode> deleteUserAns(Long userAnsId, String authorization);

    ResponseEntity<CustomResponseCode> deleteFriend(Long friendId, String authorization);

}

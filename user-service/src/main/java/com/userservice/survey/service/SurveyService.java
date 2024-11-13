package com.userservice.survey.service;

import com.userservice.survey.dto.*;

import java.util.List;

public interface SurveyService {

    GetQuestionsDto getAdditionalQuestions(Long surveyId, List<Long> nxtQstIds);

    GetQuestionsDto getFirstQuestions();

    GetRecommendPerfumes saveSurveyResultAndGetRecommendPerfume(RequestSurveyResultDto dto);

    void saveSatisfactionResult(SatisfactionResultDto dto);

    GetRecommendPerfumes getRecommendPerfumeResult(Long memberId);

    GetRecommendPerfumes saveFriendSurveyResultAndGetRecommendPerfume(RequestFriendSurveyResultDto dto);

    GetRecommendPerfumes getFriendRecommendPerfumeResult(Long memberId);

}

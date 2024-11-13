package com.adminservice.survey.repository;

import com.adminservice.survey.entity.SurveyQst;
import com.adminservice.survey.entity.SurveyQstOrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyQstRepository extends JpaRepository<SurveyQst, Long>, JpaSpecificationExecutor<SurveyQst> {
    // 단일 질문 찾기
    Optional<SurveyQst> findBySurveyIdAndQuestionOrder(Long surveyId, SurveyQstOrderState state);
    // 여러 질문 찾기
    List<SurveyQst> findAllBySurveyIdAndQuestionOrder(Long surveyId, SurveyQstOrderState state);

    // 자체 id로 조회
    Optional<SurveyQst> findByIdAndQuestionOrder(Long surveyQstId, SurveyQstOrderState state);

    List<SurveyQst> findAllBySurveyId(Long surveyId);
}

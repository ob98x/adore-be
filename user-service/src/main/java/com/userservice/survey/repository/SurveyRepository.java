package com.userservice.survey.repository;

import com.userservice.survey.entity.Survey;
import com.userservice.survey.entity.SurveyState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long>, JpaSpecificationExecutor<Survey> {
    Optional<Survey> findByIdAndState(Long id, SurveyState state);
    Optional<Survey> findByState(SurveyState state);
    Optional<Survey> findTopByStateOrderByCreatedAt(SurveyState state);
}

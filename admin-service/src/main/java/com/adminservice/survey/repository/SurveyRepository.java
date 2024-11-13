package com.adminservice.survey.repository;

import com.adminservice.survey.entity.Survey;
import com.adminservice.survey.entity.SurveyState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long>, JpaSpecificationExecutor<Survey> {
    Optional<Survey> findByIdAndState(Long id, SurveyState state);
    Optional<Survey> findByState(SurveyState state);
}

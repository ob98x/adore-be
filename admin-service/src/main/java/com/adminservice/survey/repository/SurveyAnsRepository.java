package com.adminservice.survey.repository;

import com.adminservice.survey.entity.SurveyAns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyAnsRepository extends JpaRepository<SurveyAns, Long>, JpaSpecificationExecutor<SurveyAns> {
    List<SurveyAns> findAllBySurveyQstId(Long surveyQstId);

    void deleteAllBySurveyQstId(Long surveyQstId);
}

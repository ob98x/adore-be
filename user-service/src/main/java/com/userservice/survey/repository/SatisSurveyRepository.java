package com.userservice.survey.repository;

import com.userservice.survey.entity.SatisSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SatisSurveyRepository extends JpaRepository<SatisSurvey, Long>, JpaSpecificationExecutor<SatisSurvey> {
    Optional<SatisSurvey> findByUserAnsId(Long userAnsId);
}

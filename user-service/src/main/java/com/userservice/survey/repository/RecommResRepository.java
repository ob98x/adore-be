package com.userservice.survey.repository;

import com.userservice.survey.entity.RecommRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommResRepository extends JpaRepository<RecommRes, Long>, JpaSpecificationExecutor<RecommRes> {
    List<RecommRes> findAllByUserAnsId(Long userAnsId);
}

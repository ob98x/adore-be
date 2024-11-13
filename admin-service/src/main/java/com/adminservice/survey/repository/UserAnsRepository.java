package com.adminservice.survey.repository;

import com.adminservice.survey.entity.UserAns;
import com.adminservice.survey.entity.UserAnsState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserAnsRepository extends JpaRepository<UserAns, Long>, JpaSpecificationExecutor<UserAns> {
    Optional<UserAns> findByIdAndState(Long id, UserAnsState state);

    //가장 최근에 작성한 설문 조회
    Optional<UserAns> findByMemberIdAndStateOrderByCreatedAtDesc(Long memberId, UserAnsState state);

    // 특정 기간 내 추천 기능 이용자 수(설문 기능 이용자 수) 조회
    @Query("SELECT COUNT(s) FROM UserAns s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    Long countRecommendUsersBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

package com.adminservice.survey.repository;

import com.adminservice.statics.dto.CountList;
import com.adminservice.statics.dto.DateCountDto;
import com.adminservice.survey.entity.UserAns;
import com.adminservice.survey.entity.UserAnsState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnsRepository extends JpaRepository<UserAns, Long>, JpaSpecificationExecutor<UserAns> {
    Optional<UserAns> findByIdAndState(Long id, UserAnsState state);

    //가장 최근에 작성한 설문 조회
    Optional<UserAns> findByMemberIdAndStateOrderByCreatedAtDesc(Long memberId, UserAnsState state);

    @Query("SELECT new com.adminservice.statics.dto.CountList(DATE(m.createdAt), COUNT(m)) " +
            "FROM UserAns m " +
            "WHERE m.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(m.createdAt) " +
            "ORDER BY DATE(m.createdAt)")
    List<CountList> findCreatedCountByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}

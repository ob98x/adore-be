package com.adminservice.user.repository;

import com.adminservice.statics.dto.CountList;
import com.adminservice.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Optional<Member> findById(Long id);
    boolean existsMemberByEmail(String email);
    boolean existsMemberByNickname(String nickname);

    @Query(value = "SELECT DATE(m.created_at) as date, COUNT(*) as count " +
            "FROM member m " +
            "WHERE m.created_at BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(m.created_at) " +
            "ORDER BY DATE(m.created_at)",
            nativeQuery = true)
    List<CountList> findCreatedUserCountByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.adminservice.statics.dto.CountList(u.lastLoginAt, COUNT(u)) " +
            "FROM Member u " +
            "WHERE u.lastLoginAt BETWEEN :startDate AND :endDate " +
            "GROUP BY u.lastLoginAt " +
            "ORDER BY u.lastLoginAt")
    List<CountList> findActiveCountByDateRange(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.adminservice.statics.dto.CountList(u.lastLoginAt, COUNT(u)) " +
            "FROM Member u " +
            "WHERE u.lastLoginAt < :startDate " +
            "GROUP BY u.lastLoginAt " +
            "ORDER BY u.lastLoginAt")
    List<CountList> findInactiveCountByDateRange(@Param("startDate") LocalDate startDate);
}


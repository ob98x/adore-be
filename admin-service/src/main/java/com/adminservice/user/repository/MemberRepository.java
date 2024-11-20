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

    @Query("SELECT new com.adminservice.statics.dto.CountList(DATE(m.createdAt), COUNT(m)) " +
            "FROM Member m " +
            "WHERE m.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(m.createdAt) " +
            "ORDER BY DATE(m.createdAt)")
    List<CountList> findNewUserCountByDateRange(@Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);



    @Query("SELECT new com.adminservice.statics.dto.CountList(DATE(u.lastLoginAt), COUNT(u)) " +
            "FROM Member u " +
            "WHERE u.lastLoginAt BETWEEN :startDate AND :endDate " +
            "GROUP BY u.lastLoginAt " +
            "ORDER BY u.lastLoginAt")
    List<CountList> findActiveCountByDateRange(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.adminservice.statics.dto.CountList(DATE(u.lastLoginAt), COUNT(u)) " +
            "FROM Member u " +
            "WHERE u.lastLoginAt < :startDate " +
            "GROUP BY u.lastLoginAt " +
            "ORDER BY u.lastLoginAt")
    List<CountList> findInactiveCountByDateRange(@Param("startDate") LocalDate startDate);
}


package com.adminservice.user.repository;

import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Optional<Member> findByIdAndState(Long id, MemberState state);
    Optional<Member> findById(Long id);
    boolean existsMemberByEmail(String email);
    boolean existsMemberByNickname(String nickname);

    // 현재 접속자 수 조회
    @Query("SELECT COUNT(m) FROM Member m WHERE m.lastLoginAt BETWEEN :startDate AND :endDate")
    Long countMembersByLastLoginDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 미접속자 수 조회
    @Query("SELECT COUNT(m) FROM Member m WHERE m.lastLoginAt < :date")
    Long countInactiveMembersByDate(@Param("date") LocalDate date);

    // 특정 기간 내 회원 가입자 수 조회
    @Query("SELECT COUNT(m) FROM Member m WHERE m.createdAt BETWEEN :startDate AND :endDate")
    Long countNewMembersBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}


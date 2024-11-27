package com.adminservice.penalty.repository;

import com.adminservice.penalty.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    Optional<Penalty> findByMemberId(Long memberId);

}

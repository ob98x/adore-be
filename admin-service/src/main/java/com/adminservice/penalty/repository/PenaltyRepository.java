package com.adminservice.penalty.repository;

import com.adminservice.penalty.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
}

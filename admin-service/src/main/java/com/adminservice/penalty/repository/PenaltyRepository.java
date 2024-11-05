package com.adminservice.penalty.repository;

import com.adminservice.penalty.entity.Penalty;
import com.adminservice.report.entity.Report;
import com.adminservice.report.entity.ReportCategory;
import com.adminservice.report.entity.ReportState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
}

package com.adminservice.report.repository;

import com.adminservice.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {
    Optional<Report> findReportById(Long id);
}

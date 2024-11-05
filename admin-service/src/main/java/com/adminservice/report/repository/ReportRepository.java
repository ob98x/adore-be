package com.adminservice.report.repository;

import com.adminservice.report.entity.Report;
import com.adminservice.report.entity.ReportCategory;
import com.adminservice.report.entity.ReportState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {
    Optional<Report> findReportById(Long id);
}

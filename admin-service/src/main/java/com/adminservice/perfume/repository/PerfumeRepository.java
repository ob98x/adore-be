package com.adminservice.perfume.repository;

import com.adminservice.perfume.entity.Perfume;
import com.adminservice.perfume.entity.PerfumeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PerfumeRepository extends JpaRepository<Perfume, Long>, JpaSpecificationExecutor<Perfume> {
    Optional<Perfume> findByIdAndState(Long id, PerfumeState state);
}

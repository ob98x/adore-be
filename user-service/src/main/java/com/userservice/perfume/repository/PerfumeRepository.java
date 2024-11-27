package com.userservice.perfume.repository;


import com.userservice.perfume.entity.Perfume;
import com.userservice.perfume.entity.PerfumeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long>, JpaSpecificationExecutor<Perfume> {
    Optional<Perfume> findByIdAndState(Long id, PerfumeState state);
    List<Perfume> findTop5ByStateOrderByCreatedAtDesc(PerfumeState state);
}

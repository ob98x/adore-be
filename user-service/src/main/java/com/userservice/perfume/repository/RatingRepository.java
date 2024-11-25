package com.userservice.perfume.repository;

import com.userservice.perfume.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByMemberIdAndPerfumeId(Long memberId, Long perfumeId);
}

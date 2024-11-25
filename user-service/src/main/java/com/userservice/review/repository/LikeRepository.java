package com.userservice.review.repository;

import com.userservice.review.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    int countByReviewId(Long reviewId);
    Optional<Like> findByMemberIdAndReviewId(Long memberId, Long reviewId);
}

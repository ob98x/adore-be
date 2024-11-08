package com.userservice.review.repository;


import com.userservice.review.entity.Review;
import com.userservice.review.entity.ReviewState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    Optional<Review> findByIdAndState(Long id, ReviewState state);
}

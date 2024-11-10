package com.userservice.review.repository;


import com.userservice.review.entity.Review;
import com.userservice.review.entity.ReviewState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    List<Review> findAllByState(ReviewState state);
    Optional<Review> findByIdAndState(Long id, ReviewState state);
}

package com.userservice.review.repository;


import com.userservice.review.entity.Comment;
import com.userservice.review.entity.CommentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    Optional<Comment> findByIdAndState(Long id, CommentState state);
}

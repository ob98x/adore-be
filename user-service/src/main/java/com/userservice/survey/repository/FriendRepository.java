package com.userservice.survey.repository;

import com.userservice.survey.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long>, JpaSpecificationExecutor<Friend> {
    Optional<Friend> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}

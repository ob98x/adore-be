package com.userservice.survey.repository;

import com.userservice.survey.entity.UserAns;
import com.userservice.survey.entity.UserAnsState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAnsRepository extends JpaRepository<UserAns, Long>, JpaSpecificationExecutor<UserAns> {
    Optional<UserAns> findByIdAndState(Long id, UserAnsState state);

    //가장 최근에 작성한 설문 조회
    Optional<UserAns> findByMemberIdAndStateOrderByCreatedAtDesc(Long memberId, UserAnsState state);
}

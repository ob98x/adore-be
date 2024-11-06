package com.userservice.user.repository;

import com.userservice.user.entity.Member;
import com.userservice.user.entity.MemberState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Optional<Member> findByIdAndState(Long id, MemberState state);
    boolean existsMemberByEmail(String email);
    boolean existsMemberByNickname(String nickname);
}

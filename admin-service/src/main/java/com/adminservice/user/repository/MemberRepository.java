package com.adminservice.user.repository;

import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Optional<Member> findByIdAndState(Long id, MemberState state);
    Optional<Member> findById(Long id);
    boolean existsMemberByEmail(String email);
    boolean existsMemberByNickname(String nickname);
}

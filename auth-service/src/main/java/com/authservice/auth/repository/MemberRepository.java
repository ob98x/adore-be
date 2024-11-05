package com.authservice.auth.repository;

import com.authservice.auth.entitiy.Member;
import com.authservice.auth.entitiy.MemberState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndState(Long id, MemberState state);
    Optional<Member> findMemberByEmailAndState(String email, MemberState state);
    Optional<Member> findMemberByNicknameAndState(String nickname, MemberState state);
}

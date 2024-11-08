package com.adminservice.user.service;

import com.adminservice.global.CustomException;
import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.ResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.user.dto.GetMemberListResponseDto;
import com.adminservice.user.dto.GetMemberResponseDto;
import com.adminservice.user.dto.MemberCreateRequestDto;
import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberState;
import com.adminservice.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteMember(Long id) {
        Member member = checkConflictMember(id);
        member.setState(MemberState.INACTIVE);
        memberRepository.save(member);
        return ResponseEntity.ok(CustomResponseCode.MEMBER_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createMember(MemberCreateRequestDto memberCreateRequestDto) {
        checkDuplicateMembers(memberCreateRequestDto.getNickname(), memberCreateRequestDto.getEmail());
        memberCreateRequestDto.setPassword(encodePassword(memberCreateRequestDto.getPassword()));
        memberRepository.save(MemberCreateRequestDto.createMember(memberCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.MEMBER_CREATE_SUCCESS);

    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateMember(Long id, MemberCreateRequestDto memberCreateRequestDto) {
        checkDuplicateMembers(memberCreateRequestDto.getNickname(), memberCreateRequestDto.getEmail());
        memberRepository.save(MemberCreateRequestDto.updateMember(checkConflictMember(id), memberCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.MEMBER_UPDATE_SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public GetMemberResponseDto getMember(Long id) {
        Member member = memberRepository.findByIdAndState(id, MemberState.ACTIVE).orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
        return GetMemberResponseDto.getMemberInfo(member);
    }

    // 전체 리스트
    public GetMemberListResponseDto searchUsers(SearchType searchType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        Specification<Member> spec = Specification.where(null);

        // 검색 타입에 따라 유저 검색
        if (searchType == SearchType.NICKNAME) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("nickname"), "%" + keyword + "%"));
        } else if (searchType == SearchType.EMAIL) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("email"), "%" + keyword + "%"));
        } else {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("state"), MemberState.ACTIVE));
        }

        Page<Member> resultPage = memberRepository.findAll(spec, pageable);
        List<GetMemberListResponseDto.MemberListInfo> memberList = resultPage.getContent().stream()
                .map(GetMemberListResponseDto.MemberListInfo::fromMember)
                .toList();

        return GetMemberListResponseDto.createResponse(memberList, resultPage.getTotalPages(), resultPage.hasNext());
    }

    // 닉네임, 이메일 중복 체크
    public void checkDuplicateMembers(String nickname, String email) {
        if (memberRepository.existsMemberByNickname(nickname)) {
            throw new CustomException(ResponseCode.NICKNAME_DUPLICATE);
        }
        if (memberRepository.existsMemberByEmail(email)) {
            throw new CustomException(ResponseCode.EMAIL_DUPLICATE);
        }
    }

    public Member checkConflictMember(Long id) {
        if (memberRepository.findById(id).isEmpty()) {
            throw new CustomException(ResponseCode.MEMBER_NOT_FOUND);
        }
        if (memberRepository.findById(id).get().getState().equals(MemberState.INACTIVE)) {
            throw new CustomException(ResponseCode.MEMBER_DELETED);
        }
        return memberRepository.findById(id).get();
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}

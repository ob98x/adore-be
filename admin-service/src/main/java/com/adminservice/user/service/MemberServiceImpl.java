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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteMember(Long id) {
        log.info("[ Admin Service - deleteMember ] - 회원 삭제, id: {}", id);

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
        log.info("[ Admin Service - updateMember ] - 회원 정보 수정, id: {}", id);
        checkDuplicateMembers(memberCreateRequestDto.getNickname(), memberCreateRequestDto.getEmail());
        memberRepository.save(MemberCreateRequestDto.updateMember(checkConflictMember(id), memberCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.MEMBER_UPDATE_SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public GetMemberResponseDto getMember(Long id) {
        log.info("[ Admin Service - getMember ] - 회원 정보 조회, id: {}", id);
        Member member = checkConflictMember(id);
        return GetMemberResponseDto.getMemberInfo(member);
    }

    // 전체 리스트
    public GetMemberListResponseDto searchUsers(SearchType searchType, String keyword, int page) {
        log.info("[ Admin Service - searchUsers ] - 회원 리스트 조회, searchType: {}, keyword: {}, page: {}", searchType, keyword, page);
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        log.info("[ Admin Service - searchUsers ] - 검색 조건을 설정합니다.");
        Specification<Member> spec = Specification.where(null);

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

        log.info("[ Admin Service - searchUsers ] - 회원 리스트를 DB 에서 가져옵니다.");
        Page<Member> resultPage = memberRepository.findAll(spec, pageable);

        log.info("[ Admin Service - searchUsers ] - 회원 리스트를 DTO 로 변환합니다.");
        List<GetMemberListResponseDto.MemberListInfo> memberList = resultPage.getContent().stream()
                .map(GetMemberListResponseDto.MemberListInfo::fromMember)
                .toList();

        return GetMemberListResponseDto.createResponse(memberList, resultPage.getTotalPages(), resultPage.hasNext());
    }

    // 닉네임, 이메일 중복 체크
    public void checkDuplicateMembers(String nickname, String email) {
        log.info("[ Admin Service - checkDuplicateMembers ] - 닉네임, 이메일 중복 체크, nickname: {}, email: {}", nickname, email);
        if (memberRepository.existsMemberByNickname(nickname)) {
            log.error("[ Admin Service - checkDuplicateMembers ] - 닉네임이 중복됩니다., nickname: {}", nickname);
            throw new CustomException(ResponseCode.NICKNAME_DUPLICATE);
        }
        if (memberRepository.existsMemberByEmail(email)) {
            log.error("[ Admin Service - checkDuplicateMembers ] - 이메일이 중복됩니다., email: {}", email);
            throw new CustomException(ResponseCode.EMAIL_DUPLICATE);
        }
    }

    @Override
    public Member checkConflictMember(Long id) {
        log.info("[ Admin Service - checkConflictMember ] - 회원 정보 조회, id: {}", id);
        if (memberRepository.findById(id).isEmpty()) {
            log.error("[ Admin Service - checkConflictMember ] - 회원을 찾을 수 없습니다., id: {}", id);
            throw new CustomException(ResponseCode.MEMBER_NOT_FOUND);
        }
        if (memberRepository.findById(id).get().getState().equals(MemberState.INACTIVE)) {
            log.error("[ Admin Service - checkConflictMember ] - 삭제된 회원입니다., id: {}", id);
            throw new CustomException(ResponseCode.MEMBER_DELETED);
        }
        return memberRepository.findById(id).get();
    }

    @Override
    public void checkAuthorizeMember(Long writerId, Long requestMemberId) {
        log.info("[Admin Service - checkAuthorizeMember]: 작성자와 요청자가 일치하는 지 확인합니다. writerId: {}, requestMemberId: {}", writerId, requestMemberId);
        if (!writerId.equals(requestMemberId)) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_MEMBER);
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}

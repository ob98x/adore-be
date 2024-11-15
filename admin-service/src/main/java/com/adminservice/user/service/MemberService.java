package com.adminservice.user.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.user.dto.GetMemberListResponseDto;
import com.adminservice.user.dto.GetMemberResponseDto;
import com.adminservice.user.dto.MemberCreateRequestDto;
import com.adminservice.user.entity.Member;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MemberService {
    ResponseEntity<CustomResponseCode> createMember(MemberCreateRequestDto memberCreateRequestDto);
    ResponseEntity<CustomResponseCode> updateMember(Long id, MemberCreateRequestDto memberCreateRequestDto);
    GetMemberResponseDto getMember(Long id);
    ResponseEntity<CustomResponseCode> deleteMember(Long id);
    GetMemberListResponseDto searchUsers(SearchType searchType, String keyword, int page);
    Member checkConflictMember(Long id);
    void checkAuthorizeMember(Long writerId, Long requestMemberId);
}

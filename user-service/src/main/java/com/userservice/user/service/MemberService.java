package com.userservice.user.service;


import com.userservice.global.CustomResponseCode;
import com.userservice.user.dto.GetMyPageResponseDto;
import com.userservice.user.dto.UpdateMyPageRequestDto;
import com.userservice.user.entity.Member;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    GetMyPageResponseDto getMyPage(Long memberId);
    ResponseEntity<CustomResponseCode> updateMyPage(Long memberId, UpdateMyPageRequestDto updateMyPageRequestDto);
    Member checkConflictMember(Long id);
}

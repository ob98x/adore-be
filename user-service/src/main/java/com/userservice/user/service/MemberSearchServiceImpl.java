package com.userservice.user.service;

import com.userservice.global.CustomException;
import com.userservice.global.CustomResponseCode;
import com.userservice.global.ResponseCode;
import com.userservice.user.dto.GetMyPageResponseDto;
import com.userservice.user.dto.UpdateMyPageRequestDto;
import com.userservice.user.entity.Member;
import com.userservice.user.entity.MemberState;
import com.userservice.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSearchServiceImpl implements MemberSearchService {

    private final MemberRepository memberRepository;

    @Override
    public GetMyPageResponseDto getMyPage(Long memberId) {
        return GetMyPageResponseDto.fromMember(checkConflictMember(memberId));
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateMyPage(Long memberId, UpdateMyPageRequestDto updateMyPageRequestDto) {
        checkDuplicateMembers(updateMyPageRequestDto.getNickname(), updateMyPageRequestDto.getEmail());
        memberRepository.save(UpdateMyPageRequestDto.updateMember(checkConflictMember(memberId), updateMyPageRequestDto));
        return ResponseEntity.ok(CustomResponseCode.MY_PAGE_UPDATE_SUCCESS);
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

}

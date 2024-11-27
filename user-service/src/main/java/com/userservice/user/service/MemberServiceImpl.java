package com.userservice.user.service;


import com.userservice.feign.AdminFeignInterface;
import com.userservice.feign.AuthFeignInterface;
import com.userservice.global.CustomException;
import com.userservice.global.CustomResponseCode;
import com.userservice.global.ResponseCode;
import com.userservice.user.dto.GetMyPageResponseDto;
import com.userservice.user.dto.UpdateMyPageRequestDto;
import com.userservice.user.entity.Member;
import com.userservice.user.entity.MemberState;
import com.userservice.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AdminFeignInterface adminFeignInterface;
    private final AuthFeignInterface authFeignInterface;


    @Override
    @Transactional(readOnly = true)
    public GetMyPageResponseDto getMyPage(Long memberId) {
        log.info("[Member Service - getMyPage]: 사용자 정보 조회 요청이 들어왔습니다. id: {}", memberId);
        Member member = checkConflictMember(memberId);
        log.info("[Member Service - getMyPage]: 사용자 정보를 조회합니다. id: {}", memberId);
        GetMyPageResponseDto response = GetMyPageResponseDto.fromMember(member);
        log.info("[Member Service - getMyPage]: 사용자 정보 조회가 완료되었습니다. id: {}", response.getName());

        return response;
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateMyPage(Long memberId, UpdateMyPageRequestDto updateMyPageRequestDto) {
        log.info("[Member Service - updateMyPage]: 사용자 정보 수정 요청이 들어왔습니다. id: {}", memberId);

        checkDuplicateMembers(updateMyPageRequestDto.getNickname(), updateMyPageRequestDto.getEmail());

        log.info("[Member Service - updateMyPage]: 사용자 정보를 수정합니다. id: {}", memberId);
        memberRepository.save(UpdateMyPageRequestDto.updateMember(checkConflictMember(memberId), updateMyPageRequestDto));
        return ResponseEntity.ok(CustomResponseCode.MY_PAGE_UPDATE_SUCCESS);
    }


    // 닉네임, 이메일 중복 체크
    public void checkDuplicateMembers(String nickname, String email) {
        log.info("[Member Service - checkDuplicateMembers]: 닉네임, 이메일 중복 체크를 진행합니다. nickname: {}, email: {}", nickname, email);
        if (memberRepository.existsMemberByNickname(nickname)) {
            log.error("[Member Service - checkDuplicateMembers]: 닉네임이 중복됩니다. nickname: {}", nickname);
            throw new CustomException(ResponseCode.NICKNAME_DUPLICATE);
        }
        if (memberRepository.existsMemberByEmail(email)) {
            log.error("[Member Service - checkDuplicateMembers]: 이메일이 중복됩니다. email: {}", email);
            throw new CustomException(ResponseCode.EMAIL_DUPLICATE);
        }
    }

    @Override
    public ResponseEntity<CustomResponseCode> createQuestion(String content, String title, String category, String authorization) {
        log.info("[Member Service - createQuestion]: 문의 사항 생성 요청이 들어왔습니다.");
        Long memberId = getMemberId(authorization);
        log.info("[Member Service - createQuestion]: 문의 사항 생성을 위한 사용자 정보 조회, memberId: {}", memberId);
        Long questionId = adminFeignInterface.createQuestion(content, title, category, memberId);
        log.info("[Member Service - createQuestion]: 문의 사항 생성이 완료되었습니다. questionId: {}", questionId);
        return ResponseEntity.ok(CustomResponseCode.QUESTION_CREATE_SUCCESS);
    }

    public Member checkConflictMember(Long id) {
        log.info("[Member Service - checkConflictMember]: 사용자 정보 조회 요청이 들어왔습니다. id: {}", id);
        if (memberRepository.findByIdAndState(id, MemberState.ACTIVE).isEmpty()) {
            log.error("[Member Service - checkConflictMember]: 사용자 정보를 찾을 수 없습니다. id: {}", id);
            throw new CustomException(ResponseCode.MEMBER_NOT_FOUND);
        } else return memberRepository.findByIdAndState(id, MemberState.ACTIVE).get();
    }

    public Long getMemberId(String authorization) {
        log.info("[Review Service - getMemberId]: 헤더의 Authorization 을 Access Token 으로 변환해 Token의 정보를 받아옵니다 .authorization to token, token: {}, authorization: {}", authorization, authorization.substring(7));
        String accessToken = authorization.substring(7);
        return authFeignInterface.getInfo(accessToken).getMemberId();
    }

}

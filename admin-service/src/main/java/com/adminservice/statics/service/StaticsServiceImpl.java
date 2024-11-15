package com.adminservice.statics.service;

import com.adminservice.statics.dto.GetStaticsResponseDto;
import com.adminservice.statics.entity.StaticsClass;
import com.adminservice.survey.repository.UserAnsRepository;
import com.adminservice.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaticsServiceImpl implements StaticsService {

    private final MemberRepository memberRepository;
    private final UserAnsRepository userAnsRepository;

    @Override
    @Transactional(readOnly = true)
    public GetStaticsResponseDto getNewUserStatics(LocalDate startDate, LocalDate endDate) {
        log.info("[Statics Service - getNewUserStatics]: 신규 사용자 통계 조회 요청이 들어왔습니다. 시작일: {}, 종료일: {}", startDate, endDate);
        Long count = memberRepository.countNewMembersBetweenDates(startDate.atStartOfDay(), endDate.atStartOfDay());
        return GetStaticsResponseDto.getStaticsInfo(StaticsClass.NEW_USER, count);
    }

    @Override
    @Transactional(readOnly = true)
    public GetStaticsResponseDto getInactiveMembers() {
        log.info("[Statics Service - getInactiveMembers]: 미접속 사용자 통계 조회 요청이 들어왔습니다.");
        Long count = memberRepository.countInactiveMembersByDate(LocalDate.now());
        return GetStaticsResponseDto.getStaticsInfo(StaticsClass.NOT_ENTER, count);
    }

    @Override
    @Transactional(readOnly = true)
    public GetStaticsResponseDto getActiveMembers(LocalDate startDate, LocalDate endDate) {
        log.info("[Statics Service - getActiveMembers]: 접속 사용자 통계 조회 요청이 들어왔습니다. 시작일: {}, 종료일: {}", startDate, endDate);
        Long count = memberRepository.countMembersByLastLoginDates(startDate, endDate);
        return GetStaticsResponseDto.getStaticsInfo(StaticsClass.ACTIVE_USER, count);
    }

    @Override
    @Transactional(readOnly = true)
    public GetStaticsResponseDto getRecommendUser(LocalDate startDate, LocalDate endDate) {
        log.info("[Statics Service - getRecommendUser]: 추천 기능 이용자 통계 조회 요청이 들어왔습니다. 시작일: {}, 종료일: {}", startDate, endDate);
        Long count = userAnsRepository.countRecommendUsersBetweenDates(startDate.atStartOfDay(), endDate.atStartOfDay());
        return GetStaticsResponseDto.getStaticsInfo(StaticsClass.RECOMMEND_USER, count);
    }
}

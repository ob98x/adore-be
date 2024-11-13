package com.adminservice.statics.service;

import com.adminservice.statics.dto.GetStaticsResponseDto;
import com.adminservice.statics.entity.StaticsClass;
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

    @Override
    @Transactional(readOnly = true)
    public GetStaticsResponseDto getNewUserStatics(LocalDate startDate, LocalDate endDate) {

        Long count = memberRepository.countNewMembersBetweenDates(startDate.atStartOfDay(), endDate.atStartOfDay());
        return GetStaticsResponseDto.getStaticsInfo(StaticsClass.NEW_USER, count);
    }

    @Override
    @Transactional(readOnly = true)
    public GetStaticsResponseDto getInactiveMembers() {
        Long count = memberRepository.countInactiveMembersByDate(LocalDate.now());
        return GetStaticsResponseDto.getStaticsInfo(StaticsClass.NOT_ENTER, count);
    }

    @Override
    @Transactional(readOnly = true)
    public GetStaticsResponseDto getActiveMembers(LocalDate startDate, LocalDate endDate) {
        Long count = memberRepository.countMembersByLastLoginDates(startDate, endDate);
        return GetStaticsResponseDto.getStaticsInfo(StaticsClass.ACTIVE_USER, count);
    }

    @Override
    @Transactional(readOnly = true)
    public GetStaticsResponseDto getRecommendUser(LocalDate startDate, LocalDate endDate) {
        Long count = userAnsRepository.countRecommendUsersBetweenDates(startDate.atStartOfDay(), endDate.atStartOfDay());
        return GetStaticsResponseDto.getStaticsInfo(StaticsClass.RECOMMEND_USER, count);
    }
}

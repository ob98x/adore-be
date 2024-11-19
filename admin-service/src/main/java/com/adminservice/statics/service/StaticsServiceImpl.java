package com.adminservice.statics.service;

import com.adminservice.statics.dto.CountList;
import com.adminservice.statics.dto.DateCountDto;
import com.adminservice.statics.entity.StaticsClass;
import com.adminservice.survey.repository.UserAnsRepository;
import com.adminservice.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaticsServiceImpl implements StaticsService {

    private final MemberRepository memberRepository;
    private final UserAnsRepository userAnsRepository;

    @Override
    @Transactional(readOnly = true)
    public DateCountDto getInactiveMembers(LocalDate startDate) {
        log.info("[Statics Service - getInactiveMembers]: 미접속 사용자 통계 조회 요청이 들어왔습니다.");
        List<CountList> dataList = memberRepository.findInactiveCountByDateRange(startDate);
        return DateCountDto.builder()
                .staticsClass(StaticsClass.NOT_ENTER)
                .dateCountDtoList(dataList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DateCountDto getActiveMembers(LocalDate startDate, LocalDate endDate) {
        log.info("[Statics Service - getActiveMembers]: 접속 사용자 통계 조회 요청이 들어왔습니다.");
        List<CountList> dataList = memberRepository.findActiveCountByDateRange(startDate, endDate);
        return DateCountDto.builder()
                .staticsClass(StaticsClass.ACTIVE_USER)
                .dateCountDtoList(dataList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DateCountDto getRecommendUser(LocalDate startDate, LocalDate endDate) {
        log.info("[Statics Service - getRecommendUser]: 추천 기능 이용자 통계 조회 요청이 들어왔습니다.");
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 23:59:59
        List<CountList> dataList = userAnsRepository.findCreatedCountByDateRange(startDateTime, endDateTime);
        return DateCountDto.builder()
                .staticsClass(StaticsClass.RECOMMEND_USER)
                .dateCountDtoList(dataList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DateCountDto getNewUserStatics(LocalDate startDate, LocalDate endDate) {
        log.info("[Statics Service - getNewUserStatics]: 신규 사용자 통계 조회 요청이 들어왔습니다");
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 23:59:59
        log.info("[Statics Service - getNewUserStatics]: startDate: {}, endDate: {}", startDateTime, endDateTime);
        List<CountList> dataList = memberRepository.findCreatedUserCountByDateRange(startDateTime, endDateTime);
        log.info("[Statics Service - getNewUserStatics]: dataList: {}", dataList);
        return DateCountDto.builder()
                .staticsClass(StaticsClass.NEW_USER)
                .dateCountDtoList(dataList)
                .build();
    }
}

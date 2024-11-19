package com.adminservice.statics.service;

import com.adminservice.statics.dto.DateCountDto;

import java.time.LocalDate;

public interface StaticsService {
    DateCountDto getNewUserStatics(LocalDate startDate, LocalDate endDate);
    DateCountDto getInactiveMembers(LocalDate startDate);
    DateCountDto getActiveMembers(LocalDate startDate, LocalDate endDate);
    DateCountDto getRecommendUser(LocalDate startDate, LocalDate endDate);
}

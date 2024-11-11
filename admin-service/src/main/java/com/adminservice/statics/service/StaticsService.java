package com.adminservice.statics.service;

import com.adminservice.statics.dto.GetStaticsResponseDto;

import java.time.LocalDate;

public interface StaticsService {
    GetStaticsResponseDto getNewUserStatics(LocalDate startDate, LocalDate endDate);
    GetStaticsResponseDto getInactiveMembers();
    GetStaticsResponseDto getActiveMembers(LocalDate startDate, LocalDate endDate);
}

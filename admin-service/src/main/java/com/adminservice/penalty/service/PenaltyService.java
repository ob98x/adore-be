package com.adminservice.penalty.service;

import com.adminservice.penalty.entity.PenaltyLevel;
import com.adminservice.report.dto.GetPenaltyListResponseDto;

public interface PenaltyService {
    boolean checkPenalty(Long memberId);
    GetPenaltyListResponseDto getPenaltyMembers(PenaltyLevel penaltyLevel, int page);
}

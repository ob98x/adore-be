package com.adminservice.penalty.service;

import com.adminservice.global.CustomException;
import com.adminservice.global.ResponseCode;
import com.adminservice.penalty.entity.Penalty;
import com.adminservice.penalty.repository.PenaltyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class PenaltyServiceImpl implements PenaltyService {
    private final PenaltyRepository penaltyRepository;



    // 페널티가 존재하지 않을 경우 true
    // 페널티가 존재할 경우 만료 여부 확인
    // 만료되었을 경우 true, 만료되지 않았을 경우 false
    // 결론 : 제제 대상일 경우 false, 그렇지 않을 경우 true

    @Override
    @Transactional
    public boolean checkPenalty(Long memberId) {
        if (penaltyRepository.findByMemberId(memberId).isEmpty()) {
            log.info("[Penalty Service - checkPenalty]: 페널티가 없습니다. memberId: {}", memberId);
            return true;
        } else {
            log.info("[Penalty Service - checkPenalty]: 페널티가 존재합니다. memberId: {}", memberId);
            return expirePenalty(penaltyRepository.findByMemberId(memberId).get());
        }

    }

    // 페널티가 있으면 true, 없으면 false return
    @Transactional
    public boolean expirePenalty(Penalty penalty) {
        log.info("[Penalty Service - expirePenalty]: 페널티 만료를 확인합니다. penaltyId: {}, memberId: {},", penalty.getId(), penalty.getMember().getId());
        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        LocalDateTime createdAt = penalty.getCreatedAt(); // Penalty의 생성 시간
        boolean isExpired = false;
        log.info("[Penalty Service - expirePenalty]: 분기 처리 ... 페널티 수준: {}", penalty.getLevel().toString());
        switch (penalty.getLevel()) {
            case LOW:
                if (ChronoUnit.DAYS.between(createdAt, now) >= 1) {
                    deletePenalty(penalty); // 페널티 삭제 로직 호출
                    isExpired = true;
                    break;
                }
                log.info("[Penalty Service - expirePenalty]: 페널티 만료 처리를 진행하지 않습니다. 페널티 기간이 진행중입니다. Level: {}, 부여일: {}, 만료일: {}, penaltyId: {}, memberId: {}",
                        penalty.getLevel().toString(), createdAt.toLocalDate(), createdAt.plusDays(1).toLocalDate()
                        ,penalty.getId(), penalty.getMember().getId());
                break;
            case MIDDLE:
                if (ChronoUnit.DAYS.between(createdAt, now) >= 7) {
                    deletePenalty(penalty);
                    isExpired = true;
                    break;
                }
                log.info("[Penalty Service - expirePenalty]: 페널티 만료 처리를 진행하지 않습니다. 페널티 기간이 진행중입니다. Level: {}, 부여일: {}, 만료일: {}, penaltyId: {}, memberId: {}",
                        penalty.getLevel().toString(), createdAt.toLocalDate(), createdAt.plusDays(7).toLocalDate()
                        ,penalty.getId(), penalty.getMember().getId());
                break;
            case HIGH:
                log.info("[Penalty Service - expirePenalty]: 페널티 만료 처리를 진행하지 않습니다. 페널티 수준이 HIGH 입니다. penaltyId: {}, memberId: {}", penalty.getId(), penalty.getMember().getId());
                break;
            default:
                throw new CustomException(ResponseCode.INVALID_PENALTY_LEVEL);
        }
        return isExpired;
    }

    @Transactional
    public void deletePenalty(Penalty penalty) {
        log.info("[Penalty Service - deletePenalty]: 페널티 삭제를 시작합니다. penaltyId: {}, memberId: {}", penalty.getId(), penalty.getMember().getId());
        penaltyRepository.delete(penalty);
        log.info("[Penalty Service - deletePenalty]: 페널티 삭제가 완료되었습니다. penaltyId: {}, memberId: {}", penalty.getId(), penalty.getMember().getId());
    }



}

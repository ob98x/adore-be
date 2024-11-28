package com.adminservice.report.dto;

import com.adminservice.penalty.entity.Penalty;
import com.adminservice.penalty.entity.PenaltyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetPenaltyListResponseDto {

    @Schema(description = "페널티 목록", example = "[{\"nickname\": \"제제자 닉네임\", \"email\": \"제제자 이메일\", \"level\": \"HIGH\", \"createdAt\": \"2021-07-01T00:00:00\", \"expiredAt\": \"2021-07-08T00:00:00\"}]")
    private List<PenaltyListInfo> penaltList;

    @Schema(description = "총 페이지 수", example = "1")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "false")
    private boolean hasNext;

    @Getter
    @Setter
    public static class PenaltyListInfo {

        @Schema(description = "id", example = "1")
        private Long id;

        @Schema(description = "제제자 id", example = "1")
        private Long memberId;

        @Schema(description = "제제자 닉네임", example = "제제자 닉네임")
        private String nickname;

        @Schema(description = "제제자 이메일", example = "제제자 이메일")
        private String email;

        @Schema(description = "제제 수준", example = "HIGH")
        private PenaltyLevel level;

        @Schema(description = "생성일", example = "2021-07-01T00:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "만료일", example = "2021-07-08T00:00:00")
        private LocalDateTime expiredAt;

        // 정적 팩토리 메서드를 추가하여 변환 간소화
        public static PenaltyListInfo fromPenalty(Penalty penalty) {
            PenaltyListInfo info = new PenaltyListInfo();
            info.setId(penalty.getId());
            info.setMemberId(penalty.getMember().getId());
            info.setNickname(penalty.getMember().getNickname());
            info.setEmail(penalty.getMember().getEmail());
            info.setLevel(penalty.getLevel());
            info.setCreatedAt(penalty.getCreatedAt());
            info.setExpiredAt(penalty.getCreatedAt().plusDays(addExpiredAt(penalty.getLevel())));
            return info;
        }

        private static Integer addExpiredAt(PenaltyLevel level) {
            return switch (level) {
                case LOW -> 1;
                case MIDDLE -> 7;
                case HIGH -> 9999;
                default -> 0;
            };
        }
    }
    public static GetPenaltyListResponseDto createResponse(List<PenaltyListInfo> penaltyListInfos, int totalPages, boolean hasNext) {
        GetPenaltyListResponseDto response = new GetPenaltyListResponseDto();
        response.setPenaltList(penaltyListInfos);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }
}

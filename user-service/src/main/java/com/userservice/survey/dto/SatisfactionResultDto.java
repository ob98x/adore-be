package com.userservice.survey.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SatisfactionResultDto {
    private Long userAnsId;
    private int rating;
    private String reason;

    @Builder
    public SatisfactionResultDto(Long userAnsId, int rating, String reason) {
        this.userAnsId = userAnsId;
        this.rating = rating;
        this.reason = reason;
    }

    public static SatisfactionResultDto of(Long userAnsId, int rating, String reason) {
        return SatisfactionResultDto.builder()
                .userAnsId(userAnsId)
                .rating(rating)
                .reason(reason)
                .build();
    }
}

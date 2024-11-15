package com.adminservice.statics.dto;

import com.adminservice.statics.entity.StaticsClass;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetStaticsResponseDto {

    @Schema(description = "통계 클래스", example = "NEW_USER")
    private StaticsClass staticsClass;

    @Schema(description = "통계 수", example = "10")
    private Long count;

    @Builder
    public GetStaticsResponseDto(StaticsClass staticsClass, Long count) {
        this.staticsClass = staticsClass;
        this.count = count;
    }

    public static GetStaticsResponseDto getStaticsInfo(StaticsClass staticsClass, Long count) {
        return GetStaticsResponseDto.builder()
                .staticsClass(staticsClass)
                .count(count)
                .build();
    }

}

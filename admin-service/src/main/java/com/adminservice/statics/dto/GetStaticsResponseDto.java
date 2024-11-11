package com.adminservice.statics.dto;

import com.adminservice.statics.entity.StaticsClass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetStaticsResponseDto {
    private StaticsClass staticsClass;
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

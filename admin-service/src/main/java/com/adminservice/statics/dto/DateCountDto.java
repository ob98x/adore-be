package com.adminservice.statics.dto;

import com.adminservice.statics.entity.StaticsClass;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DateCountDto {

    @Schema(description = "통계 정보", example = "NOT_ENTER")
    private StaticsClass staticsClass;

    @Schema(description = "날짜별 카운트 리스트", example = "4")
    private List<CountList> dateCountDtoList;

    @Builder
    public DateCountDto(StaticsClass staticsClass, List<CountList> dateCountDtoList) {
        this.staticsClass = staticsClass;
        this.dateCountDtoList = dateCountDtoList;
    }

    public static DateCountDto createStaticsInfo(StaticsClass staticsClass, List<CountList> dateCountDtoList) {
        return DateCountDto.builder()
                .staticsClass(staticsClass)
                .dateCountDtoList(dateCountDtoList)
                .build();
    }


}

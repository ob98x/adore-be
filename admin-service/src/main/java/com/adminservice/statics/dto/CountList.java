package com.adminservice.statics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class CountList {
    @Schema(description = "날짜", example = "2021-08-01")
    private LocalDate date;

    @Schema(description = "카운트", example = "0")
    private Long count;

    public CountList(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }

}

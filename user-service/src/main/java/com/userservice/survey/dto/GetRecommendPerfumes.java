package com.userservice.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetRecommendPerfumes {
    private List<GetRecommendPerfume> perfumes;

    public static GetRecommendPerfumes of(List<GetRecommendPerfume> perfumes) {
        GetRecommendPerfumes values = new GetRecommendPerfumes();
        values.setPerfumes(perfumes);
        return values;
    }
}

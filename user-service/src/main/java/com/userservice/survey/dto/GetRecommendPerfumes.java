package com.userservice.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetRecommendPerfumes {
    private List<GetRecommendPerfume> perfumes;
    private Boolean hasSatisSurvey;

    public static GetRecommendPerfumes toMe(List<GetRecommendPerfume> perfumes, Boolean hasSatisSurvey) {
        GetRecommendPerfumes values = new GetRecommendPerfumes();
        values.setPerfumes(perfumes);
        values.setHasSatisSurvey(hasSatisSurvey);
        return values;
    }

    public static GetRecommendPerfumes toFriend(List<GetRecommendPerfume> perfumes) {
        GetRecommendPerfumes values = new GetRecommendPerfumes();
        values.setPerfumes(perfumes);
        return values;
    }
}

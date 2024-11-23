package com.userservice.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetRecommendPerfumes {
    private Long id;
    private List<GetRecommendPerfume> perfumes;
    private Boolean hasSatisSurvey;

    public static GetRecommendPerfumes toMe(Long userAnsId, List<GetRecommendPerfume> perfumes, Boolean hasSatisSurvey) {
        GetRecommendPerfumes values = new GetRecommendPerfumes();
        values.setId(userAnsId);
        values.setPerfumes(perfumes);
        values.setHasSatisSurvey(hasSatisSurvey);
        return values;
    }

    public static GetRecommendPerfumes toFriend(Long friendId, List<GetRecommendPerfume> perfumes) {
        GetRecommendPerfumes values = new GetRecommendPerfumes();
        values.setId(friendId);
        values.setPerfumes(perfumes);
        return values;
    }
}

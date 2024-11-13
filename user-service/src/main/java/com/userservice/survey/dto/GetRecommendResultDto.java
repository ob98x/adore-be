package com.userservice.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
// 추천 결과 향수 정보들을 전달해야 함
// 리턴을 List<Get..> 방식으로 전달
public class GetRecommendResultDto {
    private Long userAnsId;
    private List<RecommendResponse> recommendations;

    @Getter
    @Setter
    public static class RecommendResponse {
        private long perfume_id;
        private String perfume_nm;
        private double cosine_sim;

        public static RecommendResponse of(long id, String name, double sim){
            RecommendResponse info = new RecommendResponse();
            info.setPerfume_id(id);
            info.setPerfume_nm(name);
            info.setCosine_sim(sim);
            return info;
        }
    }
    public static GetRecommendResultDto of(Long userAnsId, List<RecommendResponse> recommendResponses) {
        GetRecommendResultDto info = new GetRecommendResultDto();
        info.setUserAnsId(userAnsId);
        info.setRecommendations(recommendResponses);
        return info;
    }
}
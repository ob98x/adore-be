package com.userservice.survey.dto;

import com.userservice.survey.entity.RecommRes;
import com.userservice.survey.entity.UserAns;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetSurveyResultListResponseDto {

    private List<SurveyListInfo> surveyList;
    private int totalPages;
    private boolean hasNext;

    @Getter
    @Setter
    public static class SurveyListInfo {
        private Long userAnsId;
        private String userAnsTitle;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        // 추천결과 향수 이름
        private List<RecommendPerfumeNameList> perfumeNameLists;

        public static SurveyListInfo fromUserAns(UserAns userAns, List<RecommendPerfumeNameList> list) {
            SurveyListInfo info = new SurveyListInfo();
            info.setUserAnsId(userAns.getId());
            info.setUserAnsTitle("설문"+userAns.getId()); // 임시 title
            info.setCreatedAt(userAns.getCreatedAt());
            info.setUpdatedAt(userAns.getUpdatedAt());
            info.setPerfumeNameLists(list);
            return info;
        }
    }

    public static GetSurveyResultListResponseDto createResponse(List<SurveyListInfo> surveyList, int totalPages, boolean hasNext) {
        GetSurveyResultListResponseDto response = new GetSurveyResultListResponseDto();
        response.setSurveyList(surveyList);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }
}

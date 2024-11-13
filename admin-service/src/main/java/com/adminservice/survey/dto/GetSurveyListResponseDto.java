package com.adminservice.survey.dto;

import com.adminservice.survey.entity.Survey;
import com.adminservice.survey.entity.SurveyState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetSurveyListResponseDto {
    private List<SurveyListInfo> surveyList;
    private int totalPages;
    private boolean hasNext;

    @Getter
    @Setter
    public static class SurveyListInfo {
        private Long id;
        private String name;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Integer surveyCnt;
        private SurveyState state;

        public static SurveyListInfo fromSurvey(Survey survey) {
            SurveyListInfo info = new SurveyListInfo();
            info.setId(survey.getId());
            info.setName(survey.getMember().getName());
            info.setCreatedAt(survey.getCreatedAt());
            info.setUpdatedAt(survey.getUpdatedAt());
            info.setSurveyCnt(survey.getSurveyCnt());
            info.setState(survey.getState());
            return info;
        }
    }
    public static GetSurveyListResponseDto createResponse(List<SurveyListInfo> surveyList, int totalPages, boolean hasNext) {
        GetSurveyListResponseDto response = new GetSurveyListResponseDto();
        response.setSurveyList(surveyList);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }
}

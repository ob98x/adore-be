package com.userservice.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestSurveyResultDto {
    private List<SelectNote> notes;
    private Long surveyId;
    private int price;

    public static RequestSurveyResultDto of(List<SelectNote> notes, Long surveyId, int price) {
        RequestSurveyResultDto request = new RequestSurveyResultDto();
        request.setNotes(notes);
        request.setSurveyId(surveyId);
        request.setPrice(price);
        return request;
    }
}

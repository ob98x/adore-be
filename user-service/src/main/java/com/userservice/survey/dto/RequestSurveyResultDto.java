package com.userservice.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestSurveyResultDto {
    private List<SelectNote> notes;
    private Long memberId;
    private Long surveyId;
    private int price;

    public static RequestSurveyResultDto of(List<SelectNote> notes, Long memberId, Long surveyId, int price) {
        RequestSurveyResultDto request = new RequestSurveyResultDto();
        request.setNotes(notes);
        request.setMemberId(memberId);
        request.setSurveyId(surveyId);
        request.setPrice(price);
        return request;
    }
}

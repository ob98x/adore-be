package com.adminservice.survey.dto;

import com.adminservice.survey.entity.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetSurveyResponseDto {
    private Long surveyId;
    private Long memberId; // 설문을 만든 사람
    private Integer surveyCnt;
    private SurveyState state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GetSurveyQuestion> questionList;

    @Getter
    @Setter
    public static class GetSurveyQuestion {
        private Long surveyQstId;
        private Long surveyId; // 없애는 것 고려
        private String questionTxt;
        private SurveyQstTypeState questionType;
        private SurveyQstOrderState questionOrder;
        private SurveyQstDomainState questionDomain;
        private List<GetSurveyAnswer> answerList;

        public static GetSurveyQuestion fromSurveyQst(SurveyQst surveyQst, List<GetSurveyAnswer> answerList) {
            GetSurveyQuestion qst = new GetSurveyQuestion();
            qst.setSurveyQstId(surveyQst.getId());
            qst.setSurveyId(surveyQst.getSurvey().getId());
            qst.setQuestionTxt(surveyQst.getQuestionTxt());
            qst.setQuestionType(surveyQst.getQuestionType());
            qst.setQuestionOrder(surveyQst.getQuestionOrder());
            qst.setQuestionDomain(surveyQst.getQuestionDomain());
            qst.setAnswerList(answerList);
            return qst;
        }
    }

    @Getter
    @Setter
    public static class GetSurveyAnswer {
        private Long surveyAnsId;
        private Long surveyQstId; // 없애는 것 고려
        private String answerTxt;
        private String value;
        private Long nxtQstId;

        public static GetSurveyAnswer fromSurveyAns(SurveyAns surveyAns){
            GetSurveyAnswer ans = new GetSurveyAnswer();
            ans.setSurveyAnsId(surveyAns.getId());
            ans.setSurveyQstId(surveyAns.getSurveyQst().getId());
            ans.setAnswerTxt(surveyAns.getAnswerTxt());
            ans.setValue(surveyAns.getValue());
            ans.setNxtQstId(surveyAns.getNxtQstId());
            return ans;
        }
    }

    public static GetSurveyResponseDto fromSurvey(Survey survey, List<GetSurveyQuestion> questionList){
        GetSurveyResponseDto info = new GetSurveyResponseDto();
        info.setSurveyId(survey.getId());
        info.setMemberId(survey.getMember().getId());
        info.setSurveyCnt(survey.getSurveyCnt());
        info.setState(survey.getState());
        info.setCreatedAt(survey.getCreatedAt());
        info.setUpdatedAt(survey.getUpdatedAt());
        info.setQuestionList(questionList);
        return info;
    }
//    @Builder
//    public GetSurveyResponseDto(Long surveyId, Long memberId, Integer surveyCnt, SurveyState state, LocalDateTime createdAt, LocalDateTime updatedAt) {
//        this.surveyId = surveyId;
//        this.memberId = memberId;
//        this.surveyCnt = surveyCnt;
//        this.state = state;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }
//
//    public static GetSurveyResponseDto getSurvey(Survey survey) {
//        return GetSurveyResponseDto.builder()
//                .surveyId(survey.getId())
//                .memberId(survey.getMember().getId())
//                .surveyCnt(survey.getSurveyCnt())
//                .state(survey.getState())
//                .createdAt(survey.getCreatedAt())
//                .updatedAt(survey.getUpdatedAt())
//                .build();
//    }
}

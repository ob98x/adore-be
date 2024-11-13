package com.userservice.survey.dto;

import com.userservice.survey.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetQuestionsDto {
    private List<QstAnsSet> qstAnsSets;
    private QstAnsSet qstAnsSet;
    private Long surveyId;

    @Getter
    @Setter
    public static class QstAnsSet {
        private Long qstId;
        private String qstText;
        private SurveyQstTypeState typeState;
        private SurveyQstOrderState order;
        private SurveyQstDomainState domain;
        private List<AnsSet> ansSets;

        public static QstAnsSet fromQst(SurveyQst surveyQst, List<AnsSet> ansSets) {
            QstAnsSet qstAnsSet = new QstAnsSet();
            qstAnsSet.setQstId(surveyQst.getId());
            qstAnsSet.setQstText(surveyQst.getQuestionTxt());
            qstAnsSet.setTypeState(surveyQst.getQuestionType());
            qstAnsSet.setOrder(surveyQst.getQuestionOrder());
            qstAnsSet.setDomain(surveyQst.getQuestionDomain());
            qstAnsSet.setAnsSets(ansSets);
            return qstAnsSet;
        }
    }

    @Getter
    @Setter
    public static class AnsSet {
        private String ansText;
        private Object value;
        private Long nextQstId;

        public static AnsSet fromAns(SurveyAns surveyAns) {
            AnsSet ansSet = new AnsSet();
            ansSet.setAnsText(surveyAns.getAnswerTxt());
            ansSet.setValue(surveyAns.getValue());
            ansSet.setNextQstId(surveyAns.getNxtQstId());
            return ansSet;
        }
    }

    public static GetQuestionsDto createResponses(List<QstAnsSet> qstAnsSets, Long surveyId) {
        GetQuestionsDto response = new GetQuestionsDto();
        response.setQstAnsSets(qstAnsSets);
        response.setSurveyId(surveyId);
        return response;
    }

    public static GetQuestionsDto createSingleResponse(QstAnsSet qstAnsSet, Long surveyId) {
        GetQuestionsDto response = new GetQuestionsDto();
        response.setQstAnsSet(qstAnsSet);
        response.setSurveyId(surveyId);
        return response;
    }
}

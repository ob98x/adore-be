package com.adminservice.survey.entity;

import com.adminservice.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "survey_qst")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQst extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Column(name = "question_txt")
    private String questionTxt;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private SurveyQstTypeState questionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_order")
    private SurveyQstOrderState questionOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_domain")
    private SurveyQstDomainState questionDomain;

    @Builder
    public SurveyQst(Survey survey, String questionTxt, SurveyQstTypeState questionType, SurveyQstOrderState questionOrder, SurveyQstDomainState questionDomain) {
        this.survey = survey;
        this.questionTxt = questionTxt;
        this.questionType = questionType;
        this.questionOrder = questionOrder;
        this.questionDomain = questionDomain;
    }

    public static SurveyQst of(Survey survey, String questionTxt, SurveyQstTypeState questionType, SurveyQstOrderState questionOrder, SurveyQstDomainState questionDomain) {
        return SurveyQst.builder()
                .survey(survey)
                .questionTxt(questionTxt)
                .questionType(questionType)
                .questionOrder(questionOrder)
                .questionDomain(questionDomain)
                .build();
    }
}

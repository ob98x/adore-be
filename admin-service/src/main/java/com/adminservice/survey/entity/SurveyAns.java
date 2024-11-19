package com.adminservice.survey.entity;

import com.adminservice.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "survey_ans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyAns extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_qst_id")
    private SurveyQst surveyQst;

    @Column(name = "answer_txt")
    private String answerTxt;

    @Column(name = "value")
    private String value;

    @Column(name = "nxt_qst_id")
    private Long nxtQstId;

    @Builder
    public SurveyAns(SurveyQst surveyQst, String answerTxt, String value, Long nxtQstId) {
        this.surveyQst = surveyQst;
        this.answerTxt = answerTxt;
        this.value = value;
        this.nxtQstId = nxtQstId;
    }

    public static SurveyAns of(SurveyQst surveyQst, String answerTxt, String value, Long nxtQstId) {
        return SurveyAns.builder()
                .surveyQst(surveyQst)
                .answerTxt(answerTxt)
                .value(value)
                .nxtQstId(nxtQstId)
                .build();
    }
}

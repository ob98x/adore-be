package com.adminservice.survey.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "survey_cnt")
    private Integer surveyCnt;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SurveyState state;

    @Builder
    public Survey(Member member, Integer surveyCnt, SurveyState state) {
        this.member = member;
        this.surveyCnt = surveyCnt;
        this.state = state;
    }

    public static Survey of(Member member, Integer surveyCnt, SurveyState state){
        return Survey.builder()
                .member(member)
                .surveyCnt(surveyCnt)
                .state(state)
                .build();
    }
}

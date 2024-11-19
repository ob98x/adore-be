package com.adminservice.survey.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_ans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAns extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 성별은 Member 테이블에서 추출

    @Column(name = "select_notes")
    @Convert(converter = StringListConverter.class)
    private List<String> selectNotes;

    @Column(name = "select_price")
    private int selectPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private UserAnsState state;

    @Builder
    public UserAns(Survey survey, Member member, List<String> selectNotes, int selectPrice, UserAnsState state) {
        this.survey = survey;
        this.member = member;
        this.selectNotes = selectNotes;
        this.selectPrice = selectPrice;
        this.state = state;
    }

    public static UserAns of(Survey survey, Member member, List<String> selectNotes, int selectPrice, UserAnsState state) {
        return UserAns.builder()
                .survey(survey)
                .member(member)
                .selectNotes(selectNotes)
                .selectPrice(selectPrice)
                .state(state)
                .build();
    }
}

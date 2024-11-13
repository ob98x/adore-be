package com.userservice.survey.entity;

import com.userservice.global.BaseEntity;
import com.userservice.user.entity.Member;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "user_ans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAns extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne
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

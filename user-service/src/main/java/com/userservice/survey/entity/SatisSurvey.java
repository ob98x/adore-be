package com.userservice.survey.entity;

import com.userservice.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "satis_survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SatisSurvey extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_ans_id")
    private UserAns userAns;

    @Column(name = "rating")
    private int rating;

    @Column(name = "reason")
    private String reason;

    @Builder
    public SatisSurvey(UserAns userAns, int rating, String reason) {
        this.userAns = userAns;
        this.rating = rating;
        this.reason = reason;
    }

    public static SatisSurvey of(UserAns userAns, int rating, String reason) {
        return SatisSurvey.builder()
                .userAns(userAns)
                .rating(rating)
                .reason(reason)
                .build();
    }
}

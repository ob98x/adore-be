package com.userservice.survey.entity;

import com.userservice.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "recomm_res")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommRes extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_ans_id")
    private UserAns userAns;

    @Column(name = "recomm_perfume_id")
    private long recommPerfumeId;

    @Column(name = "recomm_perfume_nm")
    private String recommPerfumeNm;

    @Column(name = "cosine_sim")
    private double cosineSim;

    @Builder
    public RecommRes(UserAns userAns, long recommPerfumeId, String recommPerfumeNm, double cosineSim) {
        this.userAns = userAns;
        this.recommPerfumeId = recommPerfumeId;
        this.recommPerfumeNm = recommPerfumeNm;
        this.cosineSim = cosineSim;
    }

    public static RecommRes of(UserAns userAns, long recommPerfumeId, String recommPerfumeNm, double cosineSim) {
        return RecommRes.builder()
                .userAns(userAns)
                .recommPerfumeId(recommPerfumeId)
                .recommPerfumeNm(recommPerfumeNm)
                .cosineSim(cosineSim)
                .build();
    }
}

package com.userservice.survey.entity;

import com.userservice.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "fr_recomm_res")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FrRecommRes extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private Friend friend;

    @Column(name = "recomm_perfume_id")
    private long recommPerfumeId;

    @Column(name = "recomm_perfume_nm")
    private String recommPerfumeNm;

    @Column(name = "cosine_sim")
    private double cosineSim;


    @Builder
    public FrRecommRes(Friend friend, long recommPerfumeId, String recommPerfumeNm, double cosineSim) {
        this.friend = friend;
        this.recommPerfumeId = recommPerfumeId;
        this.recommPerfumeNm = recommPerfumeNm;
        this.cosineSim = cosineSim;
    }

    public static FrRecommRes of(Friend friend, long recommPerfumeId, String recommPerfumeNm, double cosineSim) {
        return FrRecommRes.builder()
                .friend(friend)
                .recommPerfumeId(recommPerfumeId)
                .recommPerfumeNm(recommPerfumeNm)
                .cosineSim(cosineSim)
                .build();
    }
}

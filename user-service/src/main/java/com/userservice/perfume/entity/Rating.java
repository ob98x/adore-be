package com.userservice.perfume.entity;

import com.userservice.global.BaseEntity;
import com.userservice.user.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Table(name = "rating")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Rating extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private int rating;

    @Builder
    public Rating(Perfume perfume, Member member, int rating) {
        this.perfume = perfume;
        this.member = member;
        this.rating = rating;
    }

    public Rating update(Perfume perfume, Member member, int rating) {
        this.perfume = perfume;
        this.member = member;
        this.rating = rating;
        return this;
    }
}

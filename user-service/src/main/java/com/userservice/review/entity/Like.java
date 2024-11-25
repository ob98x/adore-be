package com.userservice.review.entity;

import com.userservice.global.BaseEntity;
import com.userservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    public Like(Member member, Review review) {
        this.member = member;
        this.review = review;
    }

    public Like update(Member member, Review review) {
        this.member = member;
        this.review = review;
        return this;
    }
}
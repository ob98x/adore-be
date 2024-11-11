package com.userservice.review.entity;

import com.userservice.global.BaseEntity;
import com.userservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
@Entity
public class Comment extends BaseEntity {
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "state")
    private CommentState state;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member writer;

    @ManyToOne
    @JoinColumn(name="review_id")
    private Review review;

    @Builder
    public Comment(String content, CommentState state, Member writer, Review review) {
        this.content = content;
        this.state = state;
        this.writer = writer;
        this.review = review;
    }

    public static Comment of(String content, CommentState state, Member writer, Review review) {
        return Comment.builder()
                .content(content)
                .state(state)
                .writer(writer)
                .review(review)
                .build();
    }
}

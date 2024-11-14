package com.userservice.review.entity;

import com.userservice.global.BaseEntity;
import com.userservice.user.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
@Entity
public class Comment extends BaseEntity {

    @Schema(description = "댓글 내용", example = "댓글 내용 1")
    @Column(name = "content", nullable = false)
    private String content;

    @Schema(description = "댓글 상태", example = "ACTIVE / INACTIVE")
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private CommentState state;

    @Schema(description = "댓글 작성자", example = "1")
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member writer;

    @Schema(description = "리뷰 ID", example = "1")
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

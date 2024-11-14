package com.userservice.review.entity;

import com.userservice.global.BaseEntity;
import com.userservice.perfume.entity.Perfume;
import com.userservice.user.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Schema(description = "향수 ID", example = "1")
    @OneToOne
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @Schema(description = "회원 ID", example = "1")
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Schema(description = "댓글 목록", example = "[{\"id\": 1, \"content\": \"댓글 내용 1\"}, {\"id\": 2, \"content\": \"댓글 내용 2\"}]")
    @OneToMany( mappedBy = "review")
    private List<Comment> commentList;

    @Schema(description = "리뷰 이미지", example = "이미지 GCS 경로")
    @Column(name = "photo")
    private String photo;

    @Schema(description = "리뷰 내용", example = "리뷰 내용")
    @Column(name = "content")
    private String content;

    @Schema(description = "리뷰 제목", example = "리뷰 제목")
    @Column(name = "title")
    private String title;

    @Schema(description = "좋아요 수", example = "1")
    @Column(name = "like_cnt")
    private int likeCnt;

    @Schema(description = "조회수", example = "1")
    @Column(name = "views")
    private int views;

    @Schema(description = "리뷰 상태", example = "ACTIVE / INACTIVE")
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ReviewState state;

    @Builder
    public Review(Perfume perfume, Member member, String photo, String content, String title, int likeCnt, int views, ReviewState state) {
        this.perfume = perfume;
        this.member = member;
        this.photo = photo;
        this.content = content;
        this.title = title;
        this.likeCnt = likeCnt;
        this.views = views;
        this.state = state;
    }

    public static Review of(Perfume perfume, Member member, String photo, String content, String title, int likeCnt, int views, ReviewState state) {
        return Review.builder()
                .perfume(perfume)
                .member(member)
                .photo(photo)
                .content(content)
                .title(title)
                .likeCnt(likeCnt)
                .views(views)
                .state(state)
                .build();
    }

}

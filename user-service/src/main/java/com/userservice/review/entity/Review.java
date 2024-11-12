package com.userservice.review.entity;

import com.userservice.global.BaseEntity;
import com.userservice.perfume.entity.Perfume;
import com.userservice.user.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany( mappedBy = "review")
    private List<Comment> commentList;

    @Column(name = "photo")
    private String photo;

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;

    @Column(name = "like_cnt")
    private int likeCnt;

    @Column(name = "views")
    private int views;

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

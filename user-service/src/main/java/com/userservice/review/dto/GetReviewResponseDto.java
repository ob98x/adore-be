package com.userservice.review.dto;

import com.userservice.review.entity.Comment;
import com.userservice.review.entity.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetReviewResponseDto {
    private Long id;
    private Long memberId;
    private String perfumeName;
    private String perfumeBrand;
    private String perfumeTop;
    private String perfumeMiddle;
    private String perfumeBase;
    private String perfumeDesc;
    private String img;
    private int likeCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Comment> commentList;

    @Builder
    public GetReviewResponseDto(Long id, Long memberId, String perfumeName, String perfumeBrand, String perfumeTop, String perfumeMiddle, String perfumeBase, String perfumeDesc, String img, int likeCnt, LocalDateTime createdAt, LocalDateTime updatedAt, List<Comment> commentList) {
        this.id = id;
        this.memberId = memberId;
        this.perfumeName = perfumeName;
        this.perfumeBrand = perfumeBrand;
        this.perfumeTop = perfumeTop;
        this.perfumeMiddle = perfumeMiddle;
        this.perfumeBase = perfumeBase;
        this.perfumeDesc = perfumeDesc;
        this.img = img;
        this.likeCnt = likeCnt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentList = commentList;
    }

    public static GetReviewResponseDto getReview(Review review) {
        return GetReviewResponseDto.builder()
                .id(review.getId())
                .perfumeName(review.getPerfume().getName())
                .perfumeBrand(review.getPerfume().getBrand())
                .perfumeTop(review.getPerfume().getTop())
                .perfumeMiddle(review.getPerfume().getMiddle())
                .perfumeBase(review.getPerfume().getBase())
                .perfumeDesc(review.getPerfume().getPerfumeDesc())
                .img(review.getPerfume().getPerfumeImg())
                .likeCnt(review.getLikeCnt())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .commentList(review.getCommentList())
                .memberId(review.getMember().getId())
                .build();
    }
}

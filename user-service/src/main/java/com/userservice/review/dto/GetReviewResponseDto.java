package com.userservice.review.dto;

import com.userservice.review.entity.Comment;
import com.userservice.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetReviewResponseDto {

    @Schema(description = "리뷰 ID", example = "1")
    private Long id;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "향수 이름", example = "샤넬 블루")
    private String perfumeName;

    @Schema(description = "향수 브랜드", example = "샤넬")
    private String perfumeBrand;

    @Schema(description = "향수 탑 노트", example = "레몬")
    private String perfumeTop;

    @Schema(description = "향수 미들 노트", example = "라벤더")
    private String perfumeMiddle;

    @Schema(description = "향수 베이스 노트", example = "베티버")
    private String perfumeBase;

    @Schema(description = "향수 설명", example = "설명")
    private String perfumeDesc;

    @Schema(description = "리뷰 이미지", example = "이미지 GCS 경로")
    private String img;

    @Schema(description = "좋아요 수", example = "1")
    private int likeCnt;

    @Schema(description = "생성일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일", example = "2021-07-01T00:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "댓글 목록", example = "[{\"id\": 1, \"content\": \"댓글 내용 1\", \"createdAt\": \"2021-07-01T00:00:00\", \"updatedAt\": \"2021-07-01T00:00:00\"}, {\"id\": 2, \"content\": \"댓글 내용 2\", \"createdAt\": \"2021-07-01T00:00:00\", \"updatedAt\": \"2021-07-01T00:00:00\"}]")
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

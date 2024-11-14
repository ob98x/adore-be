package com.userservice.review.dto;


import com.userservice.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetReviewListResponseDto {

    @Schema(description = "리뷰 목록", example = "[{\"id\": 1, \"title\": \"좋아요\", \"name\": \"홍길동\", \"likeCnt\": 1, \"email\": \"dyw1014@gachon.ac.kr\", \"createdAt\": \"2021-07-01T00:00:00\"}]")
    private List<ReviewListInfo> reviewList;
    
    @Schema(description = "총 페이지 수", example = "1")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "false")
    private boolean hasNext;
    
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
    private int perfumeDesc;


    @Getter
    @Setter
    public static class ReviewListInfo {

        @Schema(description = "리뷰 ID", example = "1")
        private Long id;

        @Schema(description = "리뷰 제목", example = "좋아요")
        private String title;

        @Schema(description = "작성자 이름", example = "홍길동")
        private String name;

        @Schema(description = "좋아요 수", example = "1")
        private int likeCnt;

        @Schema(description = "작성자 이메일", example = "dyw1014@gachon.ac.kr")
        private String email;

        @Schema(description = "작성일", example = "2021-07-01T00:00:00")
        private LocalDateTime createdAt;

        public static ReviewListInfo fromReview(Review review) {
            ReviewListInfo info = new ReviewListInfo();
            info.setId(review.getId());
            info.setTitle(review.getTitle());
            info.setName(review.getMember().getName());
            info.setCreatedAt(review.getCreatedAt());
            info.setLikeCnt(review.getLikeCnt());
            info.setEmail(review.getMember().getEmail());
            return info;
        }
    }
    public static GetReviewListResponseDto createResponse(List<ReviewListInfo> reviewList, int totalPages, boolean hasNext) {
        GetReviewListResponseDto response = new GetReviewListResponseDto();
        response.setReviewList(reviewList);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }
}

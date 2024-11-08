package com.userservice.review.dto;


import com.userservice.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetReviewListResponseDto {
    private List<ReviewListInfo> reviewList;
    private int totalPages;
    private String perfumeName;
    private String perfumeBrand;
    private String perfumeTop;
    private String perfumeMiddle;
    private String perfumeBase;
    private int perfumeDesc;
    private boolean hasNext;

    @Getter
    @Setter
    public static class ReviewListInfo {
        private Long id;
        private String title;
        private String name;
        private int likeCnt;
        private LocalDateTime createdAt;



        public static ReviewListInfo fromReview(Review review) {
            ReviewListInfo info = new ReviewListInfo();
            info.setId(review.getId());
            info.setTitle(review.getTitle());
            info.setName(review.getMember().getName());
            info.setCreatedAt(review.getCreatedAt());
            info.setLikeCnt(review.getLikeCnt());
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

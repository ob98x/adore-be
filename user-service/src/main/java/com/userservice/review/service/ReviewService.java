package com.userservice.review.service;


import com.userservice.global.CustomResponseCode;
import com.userservice.global.SearchType;
import com.userservice.review.dto.CreateReportDto;
import com.userservice.review.dto.GetReviewListResponseDto;
import com.userservice.review.dto.GetReviewResponseDto;
import com.userservice.review.dto.ReviewCreateRequestDto;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    GetReviewListResponseDto searchReview(SearchType searchType, String keyword, int page);
    GetReviewResponseDto getReview(Long id);
    ResponseEntity<CustomResponseCode> deleteReview(String authorization, Long id);
    ResponseEntity<CustomResponseCode> createReview(String authorization, ReviewCreateRequestDto reviewCreateRequestDto);
    ResponseEntity<CustomResponseCode> updateReview(String authorization, Long id, ReviewCreateRequestDto reviewCreateRequestDto);
    ResponseEntity<CustomResponseCode> createComment(String authorization, Long reviewId, String content);
    ResponseEntity<CustomResponseCode> deleteComment(String authorization, Long commentId);
    ResponseEntity<CustomResponseCode> updateComment(String authorization, Long commentId, String content);
    ResponseEntity<CustomResponseCode> likeReview(String authorization, Long id);
    ResponseEntity<CustomResponseCode> reportContent(String authorization, CreateReportDto createReportDto);
}

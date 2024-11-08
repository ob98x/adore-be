package com.userservice.review.service;


import com.userservice.global.CustomResponseCode;
import com.userservice.global.SearchType;
import com.userservice.review.dto.GetReviewListResponseDto;
import com.userservice.review.dto.GetReviewResponseDto;
import com.userservice.review.dto.ReviewCreateRequestDto;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    GetReviewListResponseDto searchReview(SearchType searchType, String keyword, int page);
    GetReviewResponseDto getReview(Long id);
    ResponseEntity<CustomResponseCode> deleteReview(Long id);
    ResponseEntity<CustomResponseCode> createReview(ReviewCreateRequestDto reviewCreateRequestDto);
    ResponseEntity<CustomResponseCode> updateReview(Long id, ReviewCreateRequestDto reviewCreateRequestDto);
}

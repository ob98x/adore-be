package com.userservice.review.controller;

import com.userservice.global.CustomResponseCode;
import com.userservice.global.SearchType;
import com.userservice.review.dto.GetReviewListResponseDto;
import com.userservice.review.dto.GetReviewResponseDto;
import com.userservice.review.dto.ReviewCreateRequestDto;
import com.userservice.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/review")
public class ReviewController {

    private final ReviewService reviewService;


    @GetMapping("/lists/{page}")
    public ResponseEntity<GetReviewListResponseDto> searchReviews(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetReviewListResponseDto response = reviewService.searchReview(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<CustomResponseCode> createReview(
            @Valid @RequestBody ReviewCreateRequestDto reviewCreateRequestDto) {
        return reviewService.createReview(reviewCreateRequestDto);
    }

    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updateReview(
            @Valid @RequestBody ReviewCreateRequestDto reviewCreateRequestDto, @RequestParam Long id) {
        return reviewService.updateReview(id, reviewCreateRequestDto);
    }

    @GetMapping("/")
    public ResponseEntity<GetReviewResponseDto> viewReviewInfo(@RequestParam Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteReview(@RequestParam Long id) {
        return reviewService.deleteReview(id);
    }
}

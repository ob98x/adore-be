package com.userservice.review.controller;

import com.userservice.global.CustomResponseCode;
import com.userservice.global.SearchType;
import com.userservice.review.dto.GetReviewListResponseDto;
import com.userservice.review.dto.GetReviewResponseDto;
import com.userservice.review.dto.ReviewCreateRequestDto;
import com.userservice.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[사용자] 리뷰 관련 API", description = "Review API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "[미사용] 리뷰 리스트 검색 API", description = "리뷰 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetReviewListResponseDto> searchReviews(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetReviewListResponseDto response = reviewService.searchReview(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "리뷰 리스트 조회 API", description = "리뷰 리스트를 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<GetReviewListResponseDto.ReviewListInfo>> allReviews() {
        List<GetReviewListResponseDto.ReviewListInfo> response = reviewService.allReviews();
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "리뷰 생성 API", description = "리뷰를 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<CustomResponseCode> createReview(
            @Valid @RequestBody ReviewCreateRequestDto reviewCreateRequestDto) {
        return reviewService.createReview(reviewCreateRequestDto);
    }

    @Operation(summary = "리뷰 수정 API", description = "리뷰를 수정합니다.")
    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updateReview(
            @Valid @RequestBody ReviewCreateRequestDto reviewCreateRequestDto,@Parameter(description = "수정할 리뷰 id") @RequestParam Long id) {
        return reviewService.updateReview(id, reviewCreateRequestDto);
    }

    @Operation(summary = "리뷰 조회 API", description = "리뷰를 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetReviewResponseDto> viewReviewInfo(@Parameter(description = "조회할 리뷰 id") @RequestParam Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @Operation(summary = "리뷰 삭제 API", description = "리뷰를 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteReview(@Parameter(description = "삭제할 리뷰 id") @RequestParam Long id) {
        return reviewService.deleteReview(id);
    }
}

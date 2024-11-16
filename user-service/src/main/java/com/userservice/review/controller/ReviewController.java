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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[사용자] 리뷰 관련 API", description = "Review API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/review")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 리스트 검색 API", description = "리뷰 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetReviewListResponseDto> searchReviews(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            log.info("[Review Controller - searchReviews]: 검색 키워드가 제공되지 않았습니다. 전체 리스트를 반환합니다.");
            keyword = ""; // 빈 문자열 또는 서비스 로직에서 null을 처리
        }
        log.info("[Review Controller - searchReviews]: 리뷰 리스트 조회 요청이 들어왔습니다. type: {}, keyword: {}", searchType, keyword);
        GetReviewListResponseDto response = reviewService.searchReview(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    
    @Operation(summary = "리뷰 생성 API", description = "리뷰를 생성합니다.")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponseCode> createReview(
            @Parameter(description = "리뷰 작성자의 access token") @RequestHeader("Authorization") String authorization,
            ReviewCreateRequestDto reviewCreateRequestDto) {
        log.info("[Review Controller - createReview]: 리뷰 생성 요청이 들어왔습니다.");
        return reviewService.createReview(authorization, reviewCreateRequestDto);
    }

    @Operation(summary = "리뷰 조회 API", description = "리뷰를 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetReviewResponseDto> viewReviewInfo(@Parameter(description = "조회할 리뷰 id") @RequestParam Long id) {
        log.info("[Review Controller - viewReviewInfo]: {}번 리뷰의 조회 요청이 들어왔습니다.", id);
        return ResponseEntity.ok(reviewService.getReview(id));
    }


    @Operation(summary = "리뷰 수정 API", description = "리뷰를 수정합니다.")
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponseCode> updateReview(
            @Parameter(description = "리뷰 작성자의 access token") @RequestHeader("Authorization") String authorization,
            ReviewCreateRequestDto reviewCreateRequestDto,@Parameter(description = "수정할 리뷰 id") @RequestParam Long id) {
        log.info("[Review Controller - updateReview]: {}번 리뷰 수정 요청이 들어왔습니다.", id);
        return reviewService.updateReview(authorization, id, reviewCreateRequestDto);
    }

    @Operation(summary = "리뷰 삭제 API", description = "리뷰를 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteReview(
            @Parameter(description = "리뷰 작성자의 access token") @RequestHeader("Authorization") String authorization,
            @Parameter(description = "삭제할 리뷰 id") @RequestParam Long id) {
        log.info("[Review Controller - deleteReview]: {}번 리뷰의 삭제 요청이 들어왔습니다.", id);
        return reviewService.deleteReview(authorization, id);
    }

    @Operation(summary = "댓글 작성 API", description = "리뷰에 댓글을 작성합니다.")
    @PostMapping("/comment/create")
    public ResponseEntity<CustomResponseCode> createComment(
            @Parameter(description = "리뷰 작성자의 access token") @RequestHeader("Authorization") String authorization,
            @Parameter(description = "댓글을 작성할 리뷰 id") @RequestParam Long reviewId,
            @RequestBody String content) {
        log.info("[Review Controller - createComment]: 댓글 작성 요청이 들어왔습니다.");
        return reviewService.createComment(authorization, reviewId, content);
    }

    @Operation(summary = "댓글 수정 API", description = "리뷰의 댓글을 수정합니다.")
    @PatchMapping("/comment/update")
    public ResponseEntity<CustomResponseCode> updateComment(
            @Parameter(description = "리뷰 작성자의 access token") @RequestHeader("Authorization") String authorization,
            @Parameter(description = "수정할 댓글 id") @RequestParam Long commentId,
            @RequestBody String content) {
        log.info("[Review Controller - updateComment]: {}번 댓글의 수정 요청이 들어왔습니다.", commentId);
        return reviewService.updateComment(authorization, commentId, content);
    }

    @Operation(summary = "댓글 삭제 API", description = "리뷰의 댓글을 삭제합니다.")
    @DeleteMapping("/comment/delete")
    public ResponseEntity<CustomResponseCode> deleteComment(
            @Parameter(description = "리뷰 작성자의 access token") @RequestHeader("Authorization") String authorization,
            @Parameter(description = "삭제할 댓글 id") @RequestParam Long commentId) {
        log.info("[Review Controller - deleteComment]: {}번 댓글의 삭제 요청이 들어왔습니다.", commentId);
        return reviewService.deleteComment(authorization, commentId);
    }
}

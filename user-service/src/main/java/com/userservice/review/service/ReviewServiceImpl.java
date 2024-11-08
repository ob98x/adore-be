package com.userservice.review.service;


import com.userservice.global.CustomException;
import com.userservice.global.CustomResponseCode;
import com.userservice.global.ResponseCode;
import com.userservice.global.SearchType;
import com.userservice.perfume.entity.PerfumeState;
import com.userservice.perfume.service.PerfumeService;
import com.userservice.review.dto.GetReviewListResponseDto;
import com.userservice.review.dto.GetReviewResponseDto;
import com.userservice.review.dto.ReviewCreateRequestDto;
import com.userservice.review.entity.Review;
import com.userservice.review.entity.ReviewState;
import com.userservice.review.repository.ReviewRepository;
import com.userservice.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PerfumeService perfumeService;
    private final MemberService memberService;

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteReview(Long id) {
        Review review = checkConflictReview(id);
        review.setState(ReviewState.INACTIVE);
        reviewRepository.save(review);
        return ResponseEntity.ok(CustomResponseCode.REVIEW_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createReview(ReviewCreateRequestDto reviewCreateRequestDto) {
        Review review = Review.builder()
                .title(reviewCreateRequestDto.getTitle())
                .content(reviewCreateRequestDto.getContent())
                .photo(reviewCreateRequestDto.getPhoto())
                .perfume(perfumeService.checkConflictPerfume(reviewCreateRequestDto.getPerfumeId()))
                .member(memberService.checkConflictMember(reviewCreateRequestDto.getMemberId()))
                .build();
        reviewRepository.save(review);
        return ResponseEntity.ok(CustomResponseCode.REVIEW_CREATE_SUCCESS);

    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateReview(Long id, ReviewCreateRequestDto reviewCreateRequestDto) {
        reviewRepository.save(ReviewCreateRequestDto.updateReview(checkConflictReview(id), reviewCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.REVIEW_UPDATE_SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public GetReviewResponseDto getReview(Long id) {
        return GetReviewResponseDto.getReview(checkConflictReview(id));
    }

    // 전체 리스트
    public GetReviewListResponseDto searchReview(SearchType searchType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        Specification<Review> spec = Specification.where(null);

        // 검색 타입에 따라 유저 검색
        if (searchType == SearchType.TITLE) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("title"), "%" + keyword + "%"));
        }  else {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("state"), PerfumeState.ACTIVE));
        }

        Page<Review> resultPage = reviewRepository.findAll(spec, pageable);
        List<GetReviewListResponseDto.ReviewListInfo> reviewList = resultPage.getContent().stream()
                .map(GetReviewListResponseDto.ReviewListInfo::fromReview)
                .toList();

        return GetReviewListResponseDto.createResponse(reviewList, resultPage.getTotalPages(), resultPage.hasNext());
    }


    public Review checkConflictReview(Long id) {
        if (reviewRepository.findByIdAndState(id, ReviewState.ACTIVE).isEmpty()) {
            throw new CustomException(ResponseCode.REVIEW_NOT_FOUND);
        } else  return reviewRepository.findByIdAndState(id, ReviewState.ACTIVE).get();
    }
}

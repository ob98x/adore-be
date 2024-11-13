package com.userservice.review.service;


import com.userservice.global.*;
import com.userservice.perfume.entity.PerfumeState;
import com.userservice.perfume.service.PerfumeService;
import com.userservice.review.dto.GetReviewListResponseDto;
import com.userservice.review.dto.GetReviewResponseDto;
import com.userservice.review.dto.ReviewCreateRequestDto;
import com.userservice.review.entity.Comment;
import com.userservice.review.entity.CommentState;
import com.userservice.review.entity.Review;
import com.userservice.review.entity.ReviewState;
import com.userservice.review.repository.CommentRepository;
import com.userservice.review.repository.ReviewRepository;
import com.userservice.user.entity.Member;
import com.userservice.user.repository.MemberRepository;
import com.userservice.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final PerfumeService perfumeService;
    private final MemberService memberService;
    private final FileManager fileManager;


    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createComment(Long reviewId, Long memberId, String content) {
        Member member = memberService.checkConflictMember(memberId);
        Review review = checkConflictReview(reviewId);
        Comment comment = Comment.builder()
                .content(content)
                .review(review)
                .state(CommentState.ACTIVE)
                .writer(member)
                .build();
        commentRepository.save(comment);
        return ResponseEntity.ok(CustomResponseCode.COMMENT_CREATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteComment(Long commentId) {
        Comment comment = checkConflictComment(commentId);
        comment.setState(CommentState.INACTIVE);
        commentRepository.save(comment);
        return ResponseEntity.ok(CustomResponseCode.COMMENT_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateComment(Long commendId, String content) {
        Comment comment = checkConflictComment(commendId);
        comment.setContent(content);
        commentRepository.save(comment);
        return ResponseEntity.ok(CustomResponseCode.COMMENT_UPDATE_SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetReviewListResponseDto.ReviewListInfo> allReviews() {
        List<Review> reviewList = reviewRepository.findAllByState(ReviewState.ACTIVE);
        return reviewList.stream()
                .map(GetReviewListResponseDto.ReviewListInfo::fromReview)
                .toList();
    }

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
        MultipartFile file = reviewCreateRequestDto.getFile();
        if (file.getContentType() == null || !file.getContentType().startsWith("image")) {
            throw new CustomException(ResponseCode.INVALID_FILE_TYPE);
        }
        String imageUri = "fail";
        try {
            imageUri = fileManager.uploadImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        reviewCreateRequestDto.setPhoto(imageUri);
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
        MultipartFile file = reviewCreateRequestDto.getFile();
        if (file.getContentType() == null || !file.getContentType().startsWith("image")) {
            throw new CustomException(ResponseCode.INVALID_FILE_TYPE);
        }
        String imageUri = "fail";
        try {
            imageUri = fileManager.uploadImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        reviewCreateRequestDto.setPhoto(imageUri);
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

    public Comment checkConflictComment(Long id) {
        if (commentRepository.findByIdAndState(id, CommentState.ACTIVE).isEmpty()) {
            throw new CustomException(ResponseCode.COMMENT_NOT_FOUND);
        } else  return commentRepository.findByIdAndState(id, CommentState.ACTIVE).get();
    }
}

package com.userservice.review.service;


import com.userservice.feign.AuthFeignInterface;
import com.userservice.global.*;
import com.userservice.perfume.entity.Perfume;
import com.userservice.perfume.entity.PerfumeState;
import com.userservice.perfume.service.PerfumeService;
import com.userservice.review.dto.GetReviewListResponseDto;
import com.userservice.review.dto.GetReviewResponseDto;
import com.userservice.review.dto.ReviewCreateRequestDto;
import com.userservice.review.entity.*;
import com.userservice.review.repository.CommentRepository;
import com.userservice.review.repository.LikeRepository;
import com.userservice.review.repository.ReviewRepository;
import com.userservice.user.entity.Member;
import com.userservice.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final AuthFeignInterface authFeignInterface;
    private final CommentRepository commentRepository;
    private final PerfumeService perfumeService;
    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final FileManager fileManager;
    private final LikeRepository likeRepository;


    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createComment(String authorization, Long reviewId, String content) {
        log.info("[Review Service - createComment]: {}번 리뷰에 댓글을 작성합니다.", reviewId);

        Long memberId = getMemberId(authorization);
        Member member = memberService.checkConflictMember(memberId);
        Review review = checkConflictReview(reviewId);

        log.info("[Review Service - createComment]: reviewId: {}, memberId: {}, 댓글을 생성합니다.", reviewId, memberId);
        Comment comment = Comment.builder()
                .content(content)
                .review(review)
                .state(CommentState.ACTIVE)
                .writer(member)
                .build();

        log.info("[Review Service - createComment]: 댓글을 저장합니다.");
        commentRepository.save(comment);
        return ResponseEntity.ok(CustomResponseCode.COMMENT_CREATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateComment(String authorization, Long commendId, String content) {
        log.info("[Review Service - updateComment]: {}번 댓글을 수정합니다.", commendId);

        Comment comment = checkConflictComment(commendId);
        Long requestMemberId = getMemberId(authorization);
        checkAuthorizeMember(comment.getWriter().getId(), requestMemberId);

        log.info("[Review Service - updateComment]: 댓글 내용을 수정합니다. old: {}, new: {}", comment.getContent(), content);
        comment.setContent(content);

        log.info("[Review Service - updateComment]: 댓글을 수정합니다.");
        commentRepository.save(comment);
        return ResponseEntity.ok(CustomResponseCode.COMMENT_UPDATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteComment(String authorization, Long commentId) {
        log.info("[Review Service - deleteComment]: {}번 댓글을 삭제합니다.", commentId);

        Comment comment = checkConflictComment(commentId);
        Long requestMemberId = getMemberId(authorization);
        checkAuthorizeMember(comment.getWriter().getId(), requestMemberId);

        log.info("[Review Service - deleteComment]: 댓글을 삭제합니다.");
        comment.setState(CommentState.INACTIVE);

        log.info("[Review Service - deleteComment]: 댓글을 저장합니다.");
        commentRepository.save(comment);
        return ResponseEntity.ok(CustomResponseCode.COMMENT_DELETE_SUCCESS);
    }


    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteReview(String authorization, Long id) {
        log.info("[Review Service - deleteReview]: {}번 리뷰를 삭제합니다.", id);

        Review review = checkConflictReview(id);
        Long requestMemberId = getMemberId(authorization);
        checkAuthorizeMember(review.getMember().getId(), requestMemberId);

        log.info("[Review Service - deleteReview]: 리뷰를 삭제합니다.");
        review.setState(ReviewState.INACTIVE);

        log.info("[Review Service - deleteReview]: 리뷰를 저장합니다.");
        reviewRepository.save(review);
        return ResponseEntity.ok(CustomResponseCode.REVIEW_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createReview(String authorization, ReviewCreateRequestDto reviewCreateRequestDto) {
        log.info("[Review Service - createReview]: 리뷰를 생성합니다");
        log.info("[Review Service - createReview]: 파일이 있는지 확인합니다. file: {}", reviewCreateRequestDto.getFile());
        MultipartFile file = reviewCreateRequestDto.getFile();
        if (file == null) {
            log.info("[Review Service - createReview]: 파일이 없습니다.");
            reviewCreateRequestDto.setPhoto("");
            log.info("[Review Service - createReview]: 이미지를 업로드하지 않습니다.");
        } else {
            checkValidType(file);
            String imageUri = "init";
            log.info("[Review Service - createReview]: 이미지를 업로드합니다. imageUri: {}", imageUri);
            try {
                imageUri = fileManager.uploadImage(file);
            } catch (Exception e) {
                log.error("File upload failed: {}", e.getMessage(), e);
                throw new CustomException(ResponseCode.FILE_NOT_FOUND);
            }
            reviewCreateRequestDto.setPhoto(imageUri);
        }
        Perfume perfume = perfumeService.checkConflictPerfume(reviewCreateRequestDto.getPerfumeId());
        Member member = memberService.checkConflictMember(getMemberId(authorization));

        log.info("[Review Service - createReview]: 리뷰를 생성합니다. perfumeId: {}, memberId: {}", perfume.getId(), member.getId());
        Review review = Review.builder()
                .title(reviewCreateRequestDto.getTitle())
                .content(reviewCreateRequestDto.getContent())
                .photo(reviewCreateRequestDto.getPhoto())
                .perfume(perfume)
                .member(member)
                .state(ReviewState.ACTIVE)
                .build();

        log.info("[Review Service - createReview]: 리뷰를 저장합니다.");
        reviewRepository.save(review);

        return ResponseEntity.ok(CustomResponseCode.REVIEW_CREATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> likeReview(String authorization, Long id) {
        log.info("[Review Service - likeReview]: 리뷰에 좋아요를 합니다");

        log.info("[Review Service - likeReview]: 좋아요를 누른 멤버가 존재하는지 확인합니다");
        Long memberId = getMemberId(authorization);
        Member member = memberService.checkConflictMember(memberId);
        log.info("[Review Service - likeReview]: 좋아요를 누른 멤버가 존재하는지 확인합니다 id: {}", member.getId());

        log.info("[Review Service - likeReview]: 리뷰가 존재하는지 확인합니다");
        Review review = checkConflictReview(id);
        log.info("[Review Service - likeReview]: 리뷰가 존재하는지 확인합니다 id: {}", review.getId());

        if (likeRepository.findByMemberIdAndReviewId(memberId, id).isPresent()) {
            log.info("[Review Service - likeReview]: 이미 좋아요를 누른 리뷰입니다. 좋아요를 취소합니다.");
            Like like = likeRepository.findByMemberIdAndReviewId(memberId, id).get();
            likeRepository.delete(like);
            return ResponseEntity.ok(CustomResponseCode.REVIEW_LIKE_DELETE_SUCCESS);
        }

        likeRepository.save(Like.builder()
                .member(member)
                .review(review)
                .build());

        review.setLikeCnt(likeRepository.countByReviewId(review.getId()));
        reviewRepository.save(review);

        return ResponseEntity.ok(CustomResponseCode.REVIEW_LIKE_SUCCESS);

    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateReview(String authorization, Long id, ReviewCreateRequestDto reviewCreateRequestDto) {
        if (reviewCreateRequestDto.getFile().isEmpty()) {
            reviewCreateRequestDto.setPhoto("");
        } else {
            MultipartFile file = checkValidType(reviewCreateRequestDto.getFile());
            String imageUri = "init";
            log.info("[Review Service - createReview]: 이미지를 업로드합니다. imageUri: {}", imageUri);
            try {
                imageUri = fileManager.uploadImage(file);
            } catch (Exception e) {
                log.error("File upload failed: {}", e.getMessage(), e);
                throw new CustomException(ResponseCode.FILE_NOT_FOUND);
            }
            reviewCreateRequestDto.setPhoto(imageUri);
        }

        Review review = checkConflictReview(id);
        checkAuthorizeMember(review.getMember().getId(), getMemberId(authorization));

        log.info("[Review Service - updateReview]: 리뷰를 수정합니다. id: {}", id);
        Review updatedReview = ReviewCreateRequestDto.updateReview(review, reviewCreateRequestDto);
        updatedReview.setState(ReviewState.ACTIVE);
        reviewRepository.save(updatedReview);
        return ResponseEntity.ok(CustomResponseCode.REVIEW_UPDATE_SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public GetReviewResponseDto getReview(Long id) {
        log.info("[Review Service - getReview]: 리뷰를 조회합니다. id: {}", id);

        Review review = checkConflictReview(id);

        log.info("[Review Service - getReview]: 조회수를 증가시킵니다. id: {}", id);
        review.setViews(review.getViews() + 1);

        return GetReviewResponseDto.getReview(review);
    }

    // 전체 리스트
    public GetReviewListResponseDto searchReview(SearchType searchType, String keyword, int page) {
        log.info("[Review Service - searchReview]: 리뷰 리스트를 검색합니다. searchType: {}, keyword: {}, page: {}", searchType, keyword, page);
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        log.info("[Review Service - searchReview]: 검색 조건을 설정합니다.");
        Specification<Review> spec = Specification.where(null);
        if (keyword.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("state"), PerfumeState.ACTIVE));
        } else {
            if (searchType == SearchType.TITLE) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("title"), "%" + keyword + "%"));
            } else {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), PerfumeState.ACTIVE));
            }
        }

        log.info("[Review Service - searchReview]: 리뷰 리스트를 DB 에서 가져옵니다.");
        Page<Review> resultPage = reviewRepository.findAll(spec, pageable);

        log.info("[Review Service - searchReview]: 리뷰 리스트를 DTO 로 변환합니다.");
        List<GetReviewListResponseDto.ReviewListInfo> reviewList = resultPage.getContent().stream()
                .map(GetReviewListResponseDto.ReviewListInfo::fromReview)
                .toList();

        return GetReviewListResponseDto.createResponse(reviewList, resultPage.getTotalPages(), resultPage.hasNext());
    }


    public Review checkConflictReview(Long id) {
        log.info("[Review Service - checkConflictReview]: 리뷰가 존재하는지 확인합니다. id: {}", id);
        if (reviewRepository.findByIdAndState(id, ReviewState.ACTIVE).isEmpty()) {
            throw new CustomException(ResponseCode.REVIEW_NOT_FOUND);
        } else  return reviewRepository.findByIdAndState(id, ReviewState.ACTIVE).get();
    }

    public Comment checkConflictComment(Long id) {
        log.info("[Review Service - checkConflictComment]: 댓글이 존재하는지 확인합니다. id: {}", id);
        if (commentRepository.findByIdAndState(id, CommentState.ACTIVE).isEmpty()) {
            throw new CustomException(ResponseCode.COMMENT_NOT_FOUND);
        } else  return commentRepository.findByIdAndState(id, CommentState.ACTIVE).get();
    }

    public Long getMemberId(String authorization) {
        log.info("[Review Service - getMemberId]: 헤더의 Authorization 을 Access Token 으로 변환해 Token의 정보를 받아옵니다 .authorization to token, token: {}, authorization: {}", authorization, authorization.substring(7));
        String accessToken = authorization.substring(7);
        return authFeignInterface.getInfo(accessToken).getMemberId();
    }

    public void checkAuthorizeMember(Long writerId, Long requestMemberId) {
        log.info("[Review Service - checkAuthorizeMember]: 작성자와 요청자가 일치하는 지 확인합니다. writerId: {}, requestMemberId: {}", writerId, requestMemberId);
        if (!writerId.equals(requestMemberId)) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_MEMBER);
        }
    }

    public MultipartFile checkValidType(MultipartFile file) {
        log.info("[Review Service - checkValidType]: 파일이 이미지 형식인지 확인합니다. file: {}", file);
        if (file == null || file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image")) {
            throw new CustomException(ResponseCode.INVALID_FILE_TYPE);
        }
        return file;
    }
}

package com.userservice.review.dto;


import com.userservice.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ReviewCreateRequestDto {

    @Schema(description = "리뷰 제목", example = "좋아요")
    private String title;

    @Schema(description = "리뷰 내용", example = "좋아요")
    private String content;

    @Schema(description = "이미지 URI", example = "이미지 GCS 경로")
    private String photo;

    @Schema(description = "향수 ID", example = "1")
    private Long perfumeId;

    @Schema(description = "사진 업로드", example = "사진 확장자를 가지는 파일")
    private MultipartFile file;

    public static Review updateReview(Review review, ReviewCreateRequestDto reviewCreateRequestDto) {
        BeanUtils.copyProperties(reviewCreateRequestDto, review); // 필요한 경우 비밀번호 제외
        return review;
    }

}

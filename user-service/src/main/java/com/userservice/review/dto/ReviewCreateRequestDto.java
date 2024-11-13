package com.userservice.review.dto;


import com.userservice.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ReviewCreateRequestDto {
    private String title;
    private String content;
    private String photo;
    private Long perfumeId;
    private Long memberId;
    private MultipartFile file;

    public static Review updateReview(Review review, ReviewCreateRequestDto reviewCreateRequestDto) {
        BeanUtils.copyProperties(reviewCreateRequestDto, review); // 필요한 경우 비밀번호 제외
        return review;
    }

}

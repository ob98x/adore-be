package com.userservice.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReportDto {
    private Long contentId;
    private String category;
    private String title;
    private String content;
}

package com.userservice.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionCreateRequestDto {
    private String content;
    private String title;
    private String category;
}

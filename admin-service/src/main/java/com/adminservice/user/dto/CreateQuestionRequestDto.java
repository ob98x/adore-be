package com.adminservice.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuestionRequestDto {
    private String content;
    private String title;
    private String category;
}

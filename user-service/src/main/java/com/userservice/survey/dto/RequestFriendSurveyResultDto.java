package com.userservice.survey.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestFriendSurveyResultDto {
    private Long memberId;
    private String name;
    private String gender;
    private Integer age;
    private List<SelectNote> notes;
    private String character;
    private Integer price;

    @Builder
    public RequestFriendSurveyResultDto(Long memberId, String name, String gender, Integer age, List<SelectNote> notes, String character, Integer price) {
        this.memberId = memberId;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.notes = notes;
        this.character = character;
        this.price = price;
    }

    public static RequestFriendSurveyResultDto of(Long memberId, String name, String gender, Integer age, List<SelectNote> notes, String character, Integer price) {
        return RequestFriendSurveyResultDto.builder()
                .memberId(memberId)
                .name(name)
                .gender(gender)
                .age(age)
                .notes(notes)
                .character(character)
                .price(price)
                .build();
    }
}

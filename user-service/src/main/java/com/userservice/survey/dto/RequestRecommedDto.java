package com.userservice.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestRecommedDto {
    private List<SelectNote> notes;
    private int price;
    private String gender;

    public static RequestRecommedDto toFastApiServer(List<SelectNote> notes, int price, String gender) {
        RequestRecommedDto request = new RequestRecommedDto();
        request.setNotes(notes);
        request.setPrice(price);
        request.setGender(gender);
        return request;
    }
}

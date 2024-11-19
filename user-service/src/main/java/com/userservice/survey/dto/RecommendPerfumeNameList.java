package com.userservice.survey.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendPerfumeNameList {
    private String perfumeName;

    public static RecommendPerfumeNameList of(String perfumeName){
        RecommendPerfumeNameList name = new RecommendPerfumeNameList();
        name.setPerfumeName(perfumeName);
        return name;
    }
}

package com.userservice.survey.dto;

import com.userservice.perfume.entity.Perfume;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 추천 결과 향수 정보들을 전달해야 함
// 리턴을 List<Get..> 방식으로 전달
public class GetRecommendPerfume {
    private Long id;
    private String name;
    private String brand;
    private String top;
    private String middle;
    private String base;
    private String imageUrl;

    public static GetRecommendPerfume fromPerfume(Perfume perfume) {
        GetRecommendPerfume info = new GetRecommendPerfume();
        info.setId(perfume.getId());
        info.setName(perfume.getName());
        info.setBrand(perfume.getBrand());
        info.setTop(perfume.getTop());
        info.setMiddle(perfume.getMiddle());
        info.setBase(perfume.getBase());
        info.setImageUrl(perfume.getPerfumeImg());
        return info;
    }
}




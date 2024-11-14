package com.userservice.perfume.entity;

import com.userservice.global.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "perfume")
@Schema(name = "Perfume", description = "향수의 객체")
public class Perfume extends BaseEntity {

    @Schema(description = "향수 이름", example = "샤넬 블루")
    @Column(name = "name")
    private String name;

    @Schema(description = "향수 브랜드", example = "샤넬")
    @Column(name = "brand")
    private String brand;

    @Schema(description = "향수 원산지", example = "프랑스")
    @Column(name="country")
    private String country;

    @Schema(description = "향수 평점", example = "450")
    @Column(name="rating_cnt")
    private int rateCnt;

    @Schema(description = "향수 평점 수", example = "100")
    @Column(name="rating_value")
    private int rateValue;

    @Schema(description = "향수 출시년도", example = "2021")
    @Column(name="year")
    private int year;

    @Schema(description = "향수 설명", example = "설명")
    @Column(name = "perfume_desc")
    private String perfumeDesc;

    @Schema(description = "향수 이미지", example = "이미지 GCS 경로")
    @Column(name = "perfume_img")
    private String perfumeImg;

    @Schema(description = "향수 가격", example = "100000")
    @Column(name="price")
    private int price;

    @Schema(description = "향수 성별", example = "남성")
    @Column(name="gender")
    private String gender;

    @Schema(description = "향수 탑 노트", example = "레몬")
    @Column(name="top")
    private String top;

    @Schema(description = "향수 미들 노트", example = "라벤더")
    @Column(name="middle")
    private String middle;

    @Schema(description = "향수 베이스 노트", example = "베티버")
    @Column(name="base")
    private String base;

    @Schema(description = "향수 상태", example = "ACTIVE / INACTIVE")
    @Column(name="state")
    @Enumerated(EnumType.STRING)
    private PerfumeState state;

    @Builder
    public Perfume(String name, String brand, String country, int year, String perfumeDesc, String perfumeImg, int price, String gender, String top, String middle, String base, PerfumeState state) {
        this.name = name;
        this.brand = brand;
        this.country = country;
        this.year = year;
        this.perfumeDesc = perfumeDesc;
        this.perfumeImg = perfumeImg;
        this.price = price;
        this.gender = gender;
        this.top = top;
        this.middle = middle;
        this.base = base;
        this.state = state;
    }
    public static Perfume of(String name, String brand,  String country, int year, String perfumeDesc, String perfumeImg, int price,String gender, String top, String middle, String base, PerfumeState state) {
        return Perfume.builder()
                .name(name)
                .brand(brand)
                .country(country)
                .year(year)
                .perfumeDesc(perfumeDesc)
                .perfumeImg(perfumeImg)
                .price(price)
                .gender(gender)
                .top(top)
                .middle(middle)
                .base(base)
                .state(state)
                .build();
    }

}

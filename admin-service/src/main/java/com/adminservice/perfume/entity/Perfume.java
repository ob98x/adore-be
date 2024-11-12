package com.adminservice.perfume.entity;

import com.adminservice.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "perfume")
public class Perfume extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name="country")
    private String country;

    @Column(name="rating_cnt")
    private int rateCnt;

    @Column(name="rating_value")
    private int rateValue;

    @Column(name="year")
    private int year;

    @Column(name = "perfume_desc")
    private String perfumeDesc;

    @Column(name = "perfume_img")
    private String perfumeImg;

    @Column(name="price")
    private int price;

    @Column(name="gender")
    private String gender;

    @Column(name="top")
    private String top;

    @Column(name="middle")
    private String middle;

    @Column(name="base")
    private String base;

    @Enumerated(EnumType.STRING)
    @Column(name="state")
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

package com.adminservice.perfume.entity;

import com.adminservice.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    @Column(name = "description")
    private String description;

    @Column(name = "perfume_photo")
    private String perfumePhoto;

    @Column(name="like_cnt")
    private int likeCnt;

    @Column(name="price")
    private int price;

    @Column(name="gender")
    private String gender;

    @Column(name="season")
    private String season;

    @Column(name="state")
    private PerfumeState state;

    @Builder
    public Perfume(String name, String brand, String description, String perfumePhoto, int likeCnt, int price, String gender, String season) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.perfumePhoto = perfumePhoto;
        this.likeCnt = likeCnt;
        this.price = price;
        this.gender = gender;
        this.season = season;
    }
    public static Perfume of(String name, String brand, String description, String perfumePhoto, int likeCnt, int price, String gender, String season) {
        return Perfume.builder()
                .name(name)
                .brand(brand)
                .description(description)
                .perfumePhoto(perfumePhoto)
                .likeCnt(likeCnt)
                .price(price)
                .gender(gender)
                .season(season)
                .build();
    }

}

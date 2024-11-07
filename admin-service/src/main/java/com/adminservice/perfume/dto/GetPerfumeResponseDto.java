package com.adminservice.perfume.dto;

import com.adminservice.perfume.entity.Perfume;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetPerfumeResponseDto {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private String gender;
    private String season;
    private int price;
    private String perfumePhoto;
    private int likeCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public GetPerfumeResponseDto(Long id, String name, String brand, String description, String gender, String season, int price, String perfumePhoto, int likeCnt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.gender = gender;
        this.season = season;
        this.price = price;
        this.perfumePhoto = perfumePhoto;
        this.likeCnt = likeCnt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GetPerfumeResponseDto getPerfume(Perfume perfume) {
        return GetPerfumeResponseDto.builder()
                .id(perfume.getId())
                .name(perfume.getName())
                .brand(perfume.getBrand())
                .description(perfume.getDescription())
                .gender(perfume.getGender())
                .season(perfume.getSeason())
                .price(perfume.getPrice())
                .perfumePhoto(perfume.getPerfumePhoto())
                .likeCnt(perfume.getLikeCnt())
                .createdAt(perfume.getCreatedAt())
                .updatedAt(perfume.getUpdatedAt())
                .build();
    }
}

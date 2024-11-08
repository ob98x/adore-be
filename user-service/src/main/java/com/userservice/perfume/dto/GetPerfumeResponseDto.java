package com.userservice.perfume.dto;



import com.userservice.perfume.entity.Perfume;
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
    private String country;
    private String perfumeDesc;
    private String gender;
    private String top;
    private String middle;
    private String base;
    private int price;
    private String perfumePhoto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public GetPerfumeResponseDto(Long id, String name, String brand, String country, String perfumeDesc, String gender, String top, String middle, String base, int price, String perfumePhoto, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.perfumeDesc = perfumeDesc;
        this.gender = gender;
        this.top = top;
        this.middle = middle;
        this.base = base;
        this.price = price;
        this.perfumePhoto = perfumePhoto;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GetPerfumeResponseDto getPerfume(Perfume perfume) {
        return GetPerfumeResponseDto.builder()
                .id(perfume.getId())
                .name(perfume.getName())
                .brand(perfume.getBrand())
                .perfumeDesc(perfume.getPerfumeDesc())
                .gender(perfume.getGender())
                .top(perfume.getTop())
                .middle(perfume.getMiddle())
                .base(perfume.getBase())
                .price(perfume.getPrice())
                .perfumePhoto(perfume.getPerfumeImg())
                .createdAt(perfume.getCreatedAt())
                .updatedAt(perfume.getUpdatedAt())
                .build();
    }
}

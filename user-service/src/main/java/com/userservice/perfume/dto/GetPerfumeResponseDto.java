package com.userservice.perfume.dto;



import com.userservice.perfume.entity.Perfume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetPerfumeResponseDto {

    @Schema(description = "향수 ID", example = "1")
    private Long id;

    @Schema(description = "향수 이름", example = "샤넬 블루")
    private String name;

    @Schema(description = "향수 브랜드", example = "샤넬")
    private String brand;

    @Schema(description = "향수 원산지", example = "프랑스")
    private String country;

    @Schema(description = "향수 설명", example = "설명")
    private String perfumeDesc;

    @Schema(description = "향수 성별", example = "남성")
    private String gender;

    @Schema(description = "향수 탑 노트", example = "레몬")
    private String top;

    @Schema(description = "향수 미들 노트", example = "라벤더")
    private String middle;

    @Schema(description = "향수 베이스 노트", example = "베티버")
    private String base;

    @Schema(description = "향수 가격", example = "100000")
    private int price;

    @Schema(description = "향수 이미지", example = "이미지 GCS 경로")
    private String perfumePhoto;

    @Schema(description = "생성일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일", example = "2021-07-01T00:00:00")
    private LocalDateTime updatedAt;

    @Builder
    public GetPerfumeResponseDto(Long id, String name, String brand, String country, String perfumeDesc, String gender, String top, String middle, String base, int price, String perfumePhoto, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.country = country;
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

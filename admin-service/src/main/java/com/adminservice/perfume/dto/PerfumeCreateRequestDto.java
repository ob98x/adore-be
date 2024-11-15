package com.adminservice.perfume.dto;

import com.adminservice.perfume.entity.Perfume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PerfumeCreateRequestDto {

    @Schema(description = "향수 이름", example = "샤넬 블루")
    private String name;

    @Schema(description = "향수 브랜드", example = "샤넬")
    private String brand;

    @Schema(description = "향수 설명", example = "설명")
    private String description;

    @Schema(description = "향수 이미지", example = "이미지 GCS 경로")
    private String perfumePhoto;

    @Schema(description = "향수 성별", example = "남성")
    private String gender;

    @Schema(description = "향수 계절", example = "봄")
    private String season;

    @Schema(description = "향수 탑 노트", example = "레몬")
    private String top;

    @Schema(description = "향수 미들 노트", example = "라벤더")
    private String middle;

    @Schema(description = "향수 베이스 노트", example = "베티버")
    private String base;

    @Schema(description = "향수 원산지", example = "프랑스")
    private String country;

    @Schema(description = "향수 가격", example = "100000")
    private int price;

    @Schema(description = "사진 업로드", example = "사진 확장자를 가지는 파일")
    private MultipartFile file;


    public static Perfume createPerfume(PerfumeCreateRequestDto perfumeCreateRequestDto) {
        return Perfume.builder()
                .name(perfumeCreateRequestDto.getName())
                .brand(perfumeCreateRequestDto.getBrand())
                .perfumeDesc(perfumeCreateRequestDto.getDescription())
                .perfumeImg(perfumeCreateRequestDto.getPerfumePhoto())
                .gender(perfumeCreateRequestDto.getGender())
                .top(perfumeCreateRequestDto.getTop())
                .middle(perfumeCreateRequestDto.getMiddle())
                .base(perfumeCreateRequestDto.getBase())
                .country(perfumeCreateRequestDto.getCountry())
                .price(perfumeCreateRequestDto.getPrice())
                .build();
    }

    public static Perfume updatePerfume(Perfume perfume, PerfumeCreateRequestDto perfumeCreateRequestDto) {
        BeanUtils.copyProperties(perfumeCreateRequestDto, perfume); // 필요한 경우 비밀번호 제외
        return perfume;
    }

}

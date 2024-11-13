package com.adminservice.perfume.dto;

import com.adminservice.perfume.entity.Perfume;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PerfumeCreateRequestDto {
    private String name;
    private String brand;
    private String description;
    private String perfumePhoto;
    private String gender;
    private String season;
    private String top;
    private String middle;
    private String base;
    private String country;
    private int price;
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

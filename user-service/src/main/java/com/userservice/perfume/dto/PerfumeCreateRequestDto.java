package com.userservice.perfume.dto;

import com.userservice.perfume.entity.Perfume;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

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
    private int price;

    public static Perfume createPerfume(PerfumeCreateRequestDto perfumeCreateRequestDto) {
        return Perfume.builder()
                .name(perfumeCreateRequestDto.getName())
                .brand(perfumeCreateRequestDto.getBrand())
                .description(perfumeCreateRequestDto.getDescription())
                .perfumePhoto(perfumeCreateRequestDto.getPerfumePhoto())
                .gender(perfumeCreateRequestDto.getGender())
                .season(perfumeCreateRequestDto.getSeason())
                .price(perfumeCreateRequestDto.getPrice())
                .build();
    }

    public static Perfume updatePerfume(Perfume perfume, PerfumeCreateRequestDto perfumeCreateRequestDto) {
        BeanUtils.copyProperties(perfumeCreateRequestDto, perfume); // 필요한 경우 비밀번호 제외
        return perfume;
    }

}

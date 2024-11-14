package com.userservice.perfume.dto;

import com.userservice.perfume.entity.Perfume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetPerfumeListResponseDto {

    @Schema(description = "향수 목록", example = "[{\"id\": 1, \"name\": \"향수 이름\", \"brand\": \"향수 브랜드\", \"createdAt\": \"2021-07-01T00:00:00\", \"updatedAt\": \"2021-07-01T00:00:00\"}]")
    private List<PerfumeListInfo> perfumeList;

    @Schema(description = "총 페이지 수", example = "1")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "false")
    private boolean hasNext;

    @Getter
    @Setter
    public static class PerfumeListInfo {

        @Schema(description = "향수 ID", example = "1")
        private Long id;

        @Schema(description = "향수 이름", example = "향수 이름")
        private String name;

        @Schema(description = "향수 브랜드", example = "향수 브랜드")
        private String brand;

        @Schema(description = "생성일", example = "2021-07-01T00:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "수정일", example = "2021-07-01T00:00:00")
        private LocalDateTime updatedAt;

        public static PerfumeListInfo fromPerfume(Perfume perfume) {
            PerfumeListInfo info = new PerfumeListInfo();
            info.setId(perfume.getId());
            info.setName(perfume.getName());
            info.setBrand(perfume.getBrand());
            info.setCreatedAt(perfume.getCreatedAt());
            info.setUpdatedAt(perfume.getUpdatedAt());
            return info;
        }
    }
    public static GetPerfumeListResponseDto createResponse(List<PerfumeListInfo> perfumeList, int totalPages, boolean hasNext) {
        GetPerfumeListResponseDto response = new GetPerfumeListResponseDto();
        response.setPerfumeList(perfumeList);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }
}

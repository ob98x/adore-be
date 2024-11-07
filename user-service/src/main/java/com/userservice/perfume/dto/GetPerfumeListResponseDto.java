package com.userservice.perfume.dto;

import com.userservice.perfume.entity.Perfume;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetPerfumeListResponseDto {
    private List<PerfumeListInfo> perfumeList;
    private int totalPages;
    private boolean hasNext;

    @Getter
    @Setter
    public static class PerfumeListInfo {
        private Long id;
        private String name;
        private String brand;
        private LocalDateTime createdAt;
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

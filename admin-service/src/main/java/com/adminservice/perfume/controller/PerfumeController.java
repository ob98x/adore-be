package com.adminservice.perfume.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.perfume.dto.GetPerfumeListResponseDto;
import com.adminservice.perfume.dto.GetPerfumeResponseDto;
import com.adminservice.perfume.dto.PerfumeCreateRequestDto;
import com.adminservice.perfume.service.PerfumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "[관리자] 관리자 향수 관련 API", description = "Admin/perfume")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/perfume")
public class PerfumeController {

    private final PerfumeService perfumeService;


    @Operation(summary = "[미사용] 향수 리스트 검색 API", description = "향수 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetPerfumeListResponseDto> searchPerfume(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetPerfumeListResponseDto response = perfumeService.searchPerfumes(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "향수 리스트 조회 API", description = "향수 리스트를 조회합니다.")
    @GetMapping("/list/")
    public ResponseEntity<List<GetPerfumeListResponseDto.PerfumeListInfo>> searchPerfume() {
        List<GetPerfumeListResponseDto.PerfumeListInfo> response = perfumeService.allPerfumes();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "향수 생성 API", description = "향수를 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<CustomResponseCode> createPerfume(
            @Valid @RequestBody PerfumeCreateRequestDto perfumeCreateRequestDto) {
        return perfumeService.createPerfume(perfumeCreateRequestDto);
    }

    @Operation(summary = "향수 수정 API", description = "향수를 수정합니다.")
    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updatePerfume(
            @Valid @RequestBody PerfumeCreateRequestDto perfumeCreateRequestDto, @Parameter(description = "수정할 향수의 id") @RequestParam Long id) {
        return perfumeService.updatePerfume(id, perfumeCreateRequestDto);
    }

    @Operation(summary = "향수 조회 API", description = "향수를 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetPerfumeResponseDto> viewPerfumeInfo(@Parameter(description = "조회할 향수의 id") @RequestParam Long id) {
        return ResponseEntity.ok(perfumeService.getPerfume(id));
    }

    @Operation(summary = "향수 삭제 API", description = "향수를 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteMember(@Parameter(description = "삭제할 향수의 id") @RequestParam Long id) {
        return perfumeService.deletePerfume(id);
    }
}

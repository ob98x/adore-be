package com.adminservice.perfume.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.perfume.dto.GetPerfumeListResponseDto;
import com.adminservice.perfume.dto.GetPerfumeResponseDto;
import com.adminservice.perfume.dto.PerfumeCreateRequestDto;
import com.adminservice.perfume.service.PerfumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/perfume")
public class PerfumeController {

    private final PerfumeService perfumeService;


    @GetMapping("/lists/{page}")
    public ResponseEntity<GetPerfumeListResponseDto> searchPerfume(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetPerfumeListResponseDto response = perfumeService.searchPerfumes(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<CustomResponseCode> createPerfume(
            @Valid @RequestBody PerfumeCreateRequestDto perfumeCreateRequestDto) {
        return perfumeService.createPerfume(perfumeCreateRequestDto);
    }

    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updatePerfume(
            @Valid @RequestBody PerfumeCreateRequestDto perfumeCreateRequestDto, @RequestParam Long id) {
        return perfumeService.updatePerfume(id, perfumeCreateRequestDto);
    }

    @GetMapping("/")
    public ResponseEntity<GetPerfumeResponseDto> viewPerfumeInfo(@RequestParam Long id) {
        return ResponseEntity.ok(perfumeService.getPerfume(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteMember(@RequestParam Long id) {
        return perfumeService.deletePerfume(id);
    }
}

package com.adminservice.perfume.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.perfume.dto.GetPerfumeListResponseDto;
import com.adminservice.perfume.dto.GetPerfumeResponseDto;
import com.adminservice.perfume.dto.PerfumeCreateRequestDto;

import org.springframework.http.ResponseEntity;

public interface PerfumeService {
    ResponseEntity<CustomResponseCode> createPerfume(PerfumeCreateRequestDto perfumeCreateRequestDto);
    ResponseEntity<CustomResponseCode> updatePerfume(Long id, PerfumeCreateRequestDto perfumeCreateRequestDto);
    GetPerfumeResponseDto getPerfume(Long id);
    ResponseEntity<CustomResponseCode> deletePerfume(Long id);
    GetPerfumeListResponseDto searchPerfumes(SearchType searchType, String keyword, int page);
}

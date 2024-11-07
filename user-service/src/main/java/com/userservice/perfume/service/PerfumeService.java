package com.userservice.perfume.service;

import com.userservice.global.CustomResponseCode;
import com.userservice.global.SearchType;
import com.userservice.perfume.dto.GetPerfumeListResponseDto;
import com.userservice.perfume.dto.GetPerfumeResponseDto;
import org.springframework.http.ResponseEntity;

public interface PerfumeService {
    GetPerfumeResponseDto getPerfume(Long id);
    GetPerfumeListResponseDto searchPerfumes(SearchType searchType, String keyword, int page);
}

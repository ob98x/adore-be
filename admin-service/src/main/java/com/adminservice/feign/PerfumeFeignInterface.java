package com.adminservice.feign;

import com.adminservice.global.SearchType;
import com.adminservice.perfume.dto.GetNoteListResponseDto;
import com.adminservice.perfume.dto.GetNoteResponseDto;
import com.adminservice.perfume.dto.GetPerfumeListResponseDto;
import com.adminservice.perfume.dto.GetPerfumeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/api/user/perfume")
public interface PerfumeFeignInterface {
    @GetMapping("/")
    ResponseEntity<GetPerfumeResponseDto> viewPerfumeInfo(
            @RequestParam Long id
    );

    @GetMapping("/note")
    ResponseEntity<GetNoteResponseDto> viewNoteInfo(
            @RequestParam Long id);

    @GetMapping("/lists/{page}")
    ResponseEntity<GetPerfumeListResponseDto> searchPerfume(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword);

    @GetMapping("/note/lists/{page}")
    ResponseEntity<GetNoteListResponseDto> searchNotes(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword);

}

package com.userservice.perfume.controller;


import com.userservice.global.SearchType;
import com.userservice.perfume.dto.GetNoteListResponseDto;
import com.userservice.perfume.dto.GetNoteResponseDto;
import com.userservice.perfume.dto.GetPerfumeListResponseDto;
import com.userservice.perfume.dto.GetPerfumeResponseDto;
import com.userservice.perfume.service.PerfumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[사용자] 향수 관련 API", description = "Perfume API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/perfume")
@Slf4j
public class PerfumeController {

    private final PerfumeService perfumeService;

    @Operation(summary = "향수 정보 조회 API", description = "향수 정보를 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetPerfumeResponseDto> viewPerfumeInfo(@Parameter(description = "조회할 향수 id")@RequestParam Long id) {
        log.info("[Perfume Controller - viewPerfumeInfo]: {}번 향수의 조회 요청이 들어왔습니다.", id);
        return ResponseEntity.ok(perfumeService.getPerfume(id));
    }

    @Operation(summary = "향수 노트 정보 조회 API", description = "향수 노트 정보를 조회합니다.")
    @GetMapping("/note")
    public ResponseEntity<GetNoteResponseDto> viewNoteInfo(@Parameter(description = "조회할 노트의 id") @RequestParam Long id) {
        log.info("[Perfume Controller - viewNoteInfo]: {}번 노트의 조회 요청이 들어왔습니다.", id);
        return ResponseEntity.ok(perfumeService.getNote(id));
    }

    @Operation(summary = "향수 리스트 조회 API", description = "향수 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetPerfumeListResponseDto> searchPerfume(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam(value = "keyword", required = false) String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            log.info("[Perfume Controller - searchPerfume]: 검색 키워드가 제공되지 않았습니다. 전체 리스트를 반환합니다.");
            keyword = ""; // 빈 문자열 또는 서비스 로직에서 null을 처리
        }

        log.info("[Perfume Controller - searchPerfume]: 향수 리스트 조회 요청이 들어왔습니다. type: {}, keyword: {}", searchType, keyword);
        GetPerfumeListResponseDto response = perfumeService.searchPerfumes(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "향수 노트 리스트 조회 API", description = "향수 노트 리스트를 조회합니다.")
    @GetMapping("/note/lists/{page}")
    public ResponseEntity<GetNoteListResponseDto> searchNotes(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            log.info("[Perfume Controller - searchNotes]: 검색 키워드가 제공되지 않았습니다. 전체 리스트를 반환합니다.");
            keyword = ""; // 빈 문자열 또는 서비스 로직에서 null을 처리
        }
        log.info("[Perfume Controller - searchNotes]: 향수 노트 리스트 조회 요청이 들어왔습니다. type: {}, keyword: {}", searchType, keyword);
        GetNoteListResponseDto response = perfumeService.searchNotes(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }
}

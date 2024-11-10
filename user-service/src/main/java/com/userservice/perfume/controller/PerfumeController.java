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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[사용자] 향수 관련 API", description = "Perfume API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/perfume")
public class PerfumeController {

    private final PerfumeService perfumeService;

    @Operation(summary = "향수 정보 조회 API", description = "향수 정보를 조회합니다.")
    @GetMapping("/perfume")
    public ResponseEntity<GetPerfumeResponseDto> viewPerfumeInfo(@Parameter(description = "조회할 향수 id")@RequestParam Long id) {
        return ResponseEntity.ok(perfumeService.getPerfume(id));
    }

    @Operation(summary = "[미사용] 향수 리스트 조회 API", description = "향수 리스트를 조회합니다.")
    @GetMapping("/perfume/lists/{page}")
    public ResponseEntity<GetPerfumeListResponseDto> searchPerfume(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetPerfumeListResponseDto response = perfumeService.searchPerfumes(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "향수 리스트 조회 API", description = "향수 리스트를 조회합니다.")
    @GetMapping("/perfume/list")
    public ResponseEntity<List<GetPerfumeListResponseDto.PerfumeListInfo>> allPerfumes() {
        List<GetPerfumeListResponseDto.PerfumeListInfo> response = perfumeService.allPerfumes();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "향수 노트 정보 조회 API", description = "향수 노트 정보를 조회합니다.")
    @GetMapping("/note")
    public ResponseEntity<GetNoteResponseDto> viewNoteInfo(@Parameter(description = "조회할 노트의 id") @RequestParam Long id) {
        return ResponseEntity.ok(perfumeService.getNote(id));
    }

    @Operation(summary = "[미사용] 향수 노트 리스트 조회 API", description = "향수 노트 리스트를 조회합니다.")
    @GetMapping("/note/lists/{page}")
    public ResponseEntity<GetNoteListResponseDto> searchNotes(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetNoteListResponseDto response = perfumeService.searchNotes(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "향수 노트 리스트 조회 API", description = "향수 노트 리스트를 조회합니다.")
    @GetMapping("/note/list")
    public ResponseEntity<List<GetNoteListResponseDto.NoteListInfo>> allNotes() {
        List<GetNoteListResponseDto.NoteListInfo> response = perfumeService.allNotes();
        return ResponseEntity.ok(response);
    }

}

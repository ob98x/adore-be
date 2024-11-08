package com.userservice.perfume.controller;


import com.userservice.global.SearchType;
import com.userservice.perfume.dto.GetNoteListResponseDto;
import com.userservice.perfume.dto.GetNoteResponseDto;
import com.userservice.perfume.dto.GetPerfumeListResponseDto;
import com.userservice.perfume.dto.GetPerfumeResponseDto;
import com.userservice.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/perfume")
public class PerfumeController {

    private final PerfumeService perfumeService;

    @GetMapping("/perfume")
    public ResponseEntity<GetPerfumeResponseDto> viewPerfumeInfo(@RequestParam Long id) {
        return ResponseEntity.ok(perfumeService.getPerfume(id));
    }

    @GetMapping("/perfume/lists/{page}")
    public ResponseEntity<GetPerfumeListResponseDto> searchPerfume(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetPerfumeListResponseDto response = perfumeService.searchPerfumes(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/note")
    public ResponseEntity<GetNoteResponseDto> viewNoteInfo(@RequestParam Long id) {
        return ResponseEntity.ok(perfumeService.getNote(id));
    }

    @GetMapping("/note/lists/{page}")
    public ResponseEntity<GetNoteListResponseDto> searchNotes(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetNoteListResponseDto response = perfumeService.searchNotes(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

}

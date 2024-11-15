package com.adminservice.perfume.controller;

import com.adminservice.feign.PerfumeFeignInterface;
import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.perfume.dto.*;
import com.adminservice.perfume.service.PerfumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "[관리자] 관리자 향수 관련 API", description = "Admin/perfume")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/perfume")
@Slf4j
public class PerfumeController {

    private final PerfumeService perfumeService;
    private final PerfumeFeignInterface perfumeFeignInterface;

    @Operation(summary = "향수 생성 API", description = "향수를 생성합니다.")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponseCode> createPerfume(
           PerfumeCreateRequestDto perfumeCreateRequestDto) {
        log.info("[Perfume Controller - createPerfume]: 향수 생성 요청이 들어왔습니다.");
        return perfumeService.createPerfume(perfumeCreateRequestDto);
    }

    @Operation(summary = "향수 수정 API", description = "향수를 수정합니다.")
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponseCode> updatePerfume(
            PerfumeCreateRequestDto perfumeCreateRequestDto, @Parameter(description = "수정할 향수의 id") @RequestParam Long id) {
        log.info("[Perfume Controller - updatePerfume]: {}번 향수 수정 요청이 들어왔습니다.", id);
        return perfumeService.updatePerfume(id, perfumeCreateRequestDto);
    }

    @Operation(summary = "향수 조회 API", description = "향수를 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetPerfumeResponseDto> viewPerfumeInfo(@Parameter(description = "조회할 향수의 id") @RequestParam Long id) {
        log.info("[Perfume Controller - viewPerfumeInfo]: {}번 향수의 조회 요청이 들어왔습니다.", id);
        return ResponseEntity.ok(perfumeFeignInterface.viewPerfumeInfo(id).getBody());
    }

    @Operation(summary = "향수 삭제 API", description = "향수를 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteMember(
            @Parameter(description = "삭제할 향수의 id") @RequestParam Long id) {
        log.info("[Perfume Controller - deletePerfume]: {}번 향수 삭제 요청이 들어왔습니다.", id);
        return perfumeService.deletePerfume(id);
    }

    @Operation(summary = "향수 리스트 검색 API", description = "향수 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetPerfumeListResponseDto> searchPerfume(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        log.info("[Perfume Controller - searchPerfume]: 향수 리스트 조회 요청이 들어왔습니다. type: {}, keyword: {}", searchType, keyword);
        GetPerfumeListResponseDto response = perfumeFeignInterface.searchPerfume(page, searchType, keyword).getBody();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "노트 조회 API", description = "노트를 조회합니다.")
    @GetMapping("/note/")
    public ResponseEntity<GetNoteResponseDto> viewNoteInfo(@Parameter(description = "조회할 노트의 id") @RequestParam Long id) {
        log.info("[Perfume Controller - viewNoteInfo]: {}번 노트의 조회 요청이 들어왔습니다.", id);
        return ResponseEntity.ok(perfumeFeignInterface.viewNoteInfo(id).getBody());
    }

    @Operation(summary = "노트 등록 API", description = "노트를 등록합니다.")
    @PostMapping(value = "/note/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponseCode> createNote(
            NoteCreateRequestDto noteCreateRequestDto){
        log.info("[Perfume Controller - createNote]: 노트 생성 요청이 들어왔습니다.");
        return perfumeService.createNote(noteCreateRequestDto);
    }

    @Operation(summary = "노트 수정 API", description = "노트를 수정합니다.")
    @PatchMapping(value = "/note/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponseCode> updateNote(
            NoteCreateRequestDto noteCreateRequestDto, @Parameter(description = "수정할 노트의 id") @RequestParam Long id) {
        log.info("[Perfume Controller - updateNote]: {}번 노트 수정 요청이 들어왔습니다.", id);
        return perfumeService.updateNote(id, noteCreateRequestDto);
    }

    @Operation(summary = "노트 삭제 API", description = "노트를 삭제합니다.")
    @DeleteMapping("/note/delete")
    public ResponseEntity<CustomResponseCode> deleteNote(
            @Parameter(description = "삭제할 노트의 id") @RequestParam Long id) {
        log.info("[Perfume Controller - deleteNote]: {}번 노트 삭제 요청이 들어왔습니다.", id);
        return perfumeService.deleteNote(id);
    }
}

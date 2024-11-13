package com.adminservice.perfume.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.perfume.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PerfumeService {
    ResponseEntity<CustomResponseCode> createPerfume(PerfumeCreateRequestDto perfumeCreateRequestDto);
    ResponseEntity<CustomResponseCode> updatePerfume(Long id, PerfumeCreateRequestDto perfumeCreateRequestDto);
    ResponseEntity<CustomResponseCode> deletePerfume(Long id);
    GetPerfumeResponseDto getPerfume(Long id);
    List<GetPerfumeListResponseDto.PerfumeListInfo> allPerfumes();

    ResponseEntity<CustomResponseCode> createNote(NoteCreateRequestDto noteCreateRequestDto);
    ResponseEntity<CustomResponseCode> updateNote(Long id, NoteCreateRequestDto noteCreateRequestDto);
    ResponseEntity<CustomResponseCode> deleteNote(Long id);
    GetNoteResponseDto getNote(Long id);
    List<GetNoteListResponseDto.NoteListInfo> allNotes();

    GetPerfumeListResponseDto searchPerfumes(SearchType searchType, String keyword, int page);
}

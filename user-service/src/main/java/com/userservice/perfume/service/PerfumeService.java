package com.userservice.perfume.service;


import com.userservice.global.SearchType;
import com.userservice.perfume.dto.GetNoteListResponseDto;
import com.userservice.perfume.dto.GetNoteResponseDto;
import com.userservice.perfume.dto.GetPerfumeListResponseDto;
import com.userservice.perfume.dto.GetPerfumeResponseDto;
import com.userservice.perfume.entity.Note;
import com.userservice.perfume.entity.Perfume;

public interface PerfumeService {
    GetPerfumeResponseDto getPerfume(Long id);
    GetNoteResponseDto getNote(Long id);
    GetPerfumeListResponseDto searchPerfumes(SearchType searchType, String keyword, int page);
    GetNoteListResponseDto searchNotes(SearchType searchType, String keyword, int page);
    Perfume checkConflictPerfume(Long id);
    Note checkConflictNote(Long id);
}

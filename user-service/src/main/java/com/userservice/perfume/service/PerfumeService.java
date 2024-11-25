package com.userservice.perfume.service;


import com.userservice.global.CustomResponseCode;
import com.userservice.global.SearchType;
import com.userservice.perfume.dto.*;
import com.userservice.perfume.entity.Note;
import com.userservice.perfume.entity.Perfume;

import java.util.List;

public interface PerfumeService {
    GetPerfumeResponseDto getPerfume(Long id);
    GetNoteResponseDto getNote(Long id);
    GetPerfumeListResponseDto searchPerfumes(SearchType searchType, String keyword, int page);
    GetNoteListResponseDto searchNotes(Long parentId, int page);
    Perfume checkConflictPerfume(Long id);
    Note checkConflictNote(Long id);
    List<GetParentNoteResponseDto> getParentNotes();
    CustomResponseCode ratingPerfume(Long perfumeId, String authorization, int rating);
}

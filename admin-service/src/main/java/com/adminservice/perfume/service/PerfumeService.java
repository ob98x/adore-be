package com.adminservice.perfume.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.perfume.dto.*;
import com.adminservice.perfume.entity.Note;
import com.adminservice.perfume.entity.Perfume;
import org.springframework.http.ResponseEntity;

public interface PerfumeService {
    ResponseEntity<CustomResponseCode> createPerfume(PerfumeCreateRequestDto perfumeCreateRequestDto);
    ResponseEntity<CustomResponseCode> updatePerfume(Long id, PerfumeCreateRequestDto perfumeCreateRequestDto);
    ResponseEntity<CustomResponseCode> deletePerfume(Long id);

    ResponseEntity<CustomResponseCode> createNote(NoteCreateRequestDto noteCreateRequestDto);
    ResponseEntity<CustomResponseCode> updateNote(Long id, NoteCreateRequestDto noteCreateRequestDto);
    ResponseEntity<CustomResponseCode> deleteNote(Long id);

    Perfume checkConflictPerfume(Long id);
    Note checkConflictNote(Long id);
}

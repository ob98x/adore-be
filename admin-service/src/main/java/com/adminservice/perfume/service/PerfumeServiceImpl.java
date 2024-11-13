package com.adminservice.perfume.service;

import com.adminservice.global.*;
import com.adminservice.perfume.dto.*;
import com.adminservice.perfume.entity.Note;
import com.adminservice.perfume.entity.Perfume;
import com.adminservice.perfume.entity.PerfumeState;
import com.adminservice.perfume.repository.NoteRepository;
import com.adminservice.perfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerfumeServiceImpl implements PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final NoteRepository noteRepository;
    private final FileManager fileManager;

    @Override
    @Transactional(readOnly = true)
    public GetNoteResponseDto getNote(Long id) {
        return GetNoteResponseDto.getNote(checkConflictNote(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetNoteListResponseDto.NoteListInfo> allNotes() {
        List<Note> noteList = noteRepository.findAll();
        return noteList.stream()
                .map(GetNoteListResponseDto.NoteListInfo::fromNote)
                .toList();
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteNote(Long id) {
        Note note = checkConflictNote(id);
        noteRepository.delete(note);
        return ResponseEntity.ok(CustomResponseCode.NOTE_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createNote(NoteCreateRequestDto noteCreateRequestDto) {
        MultipartFile file = noteCreateRequestDto.getFile();
        log.info("file: {}", file);
        if (file.getContentType() == null || !file.getContentType().startsWith("image")) {
            throw new CustomException(ResponseCode.INVALID_FILE_TYPE);
        }
        String imageUri = "fail";
        try {
            imageUri = fileManager.uploadImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        noteCreateRequestDto.setNoteImg(imageUri);

        noteRepository.save(NoteCreateRequestDto.createNote(noteCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.NOTE_CREATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateNote(Long id, NoteCreateRequestDto noteCreateRequestDto) {
        MultipartFile file = noteCreateRequestDto.getFile();
        try {
            noteCreateRequestDto.setNoteImg(fileManager.uploadImage(file));
        } catch (IOException e) {
            throw new CustomException(ResponseCode.FILE_UPLOAD_FAILED);
        }
        noteRepository.save(NoteCreateRequestDto.updateNote(checkConflictNote(id), noteCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.NOTE_UPDATE_SUCCESS);
    }


    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deletePerfume(Long id) {
        Perfume perfume = checkConflictPerfume(id);
        perfume.setState(PerfumeState.INACTIVE);
        perfumeRepository.save(perfume);
        return ResponseEntity.ok(CustomResponseCode.PERFUME_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createPerfume(PerfumeCreateRequestDto perfumeCreateRequestDto) {
        MultipartFile file = perfumeCreateRequestDto.getFile();

        try {
            perfumeCreateRequestDto.setPerfumePhoto(fileManager.uploadImage(file));
        } catch (IOException e) {
            throw new CustomException(ResponseCode.FILE_UPLOAD_FAILED);
        }

        perfumeRepository.save(PerfumeCreateRequestDto.createPerfume(perfumeCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.PERFUME_CREATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updatePerfume(Long id, PerfumeCreateRequestDto perfumeCreateRequestDto) {
        MultipartFile file = perfumeCreateRequestDto.getFile();
        try {
            perfumeCreateRequestDto.setPerfumePhoto(fileManager.uploadImage(file));
        } catch (IOException e) {
            throw new CustomException(ResponseCode.FILE_UPLOAD_FAILED);
        }
        perfumeRepository.save(PerfumeCreateRequestDto.updatePerfume(checkConflictPerfume(id), perfumeCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.PERFUME_UPDATE_SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public GetPerfumeResponseDto getPerfume(Long id) {
        return GetPerfumeResponseDto.getPerfume(checkConflictPerfume(id));
    }

    @Override
    public List<GetPerfumeListResponseDto.PerfumeListInfo> allPerfumes() {
        List<Perfume> perfumes = perfumeRepository.findAll();
        return perfumes.stream()
                .map(GetPerfumeListResponseDto.PerfumeListInfo::fromPerfume)
                .toList();

    }

    // 전체 리스트
    public GetPerfumeListResponseDto searchPerfumes(SearchType searchType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        Specification<Perfume> spec = Specification.where(null);

        // 검색 타입에 따라 유저 검색
        if (searchType == SearchType.NAME) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("name"), "%" + keyword + "%"));
        } else if (searchType == SearchType.BRAND) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("brand"), "%" + keyword + "%"));
        } else {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("state"), PerfumeState.ACTIVE));
        }

        Page<Perfume> resultPage = perfumeRepository.findAll(spec, pageable);
        List<GetPerfumeListResponseDto.PerfumeListInfo> perfumeList = resultPage.getContent().stream()
                .map(GetPerfumeListResponseDto.PerfumeListInfo::fromPerfume)
                .toList();

        return GetPerfumeListResponseDto.createResponse(perfumeList, resultPage.getTotalPages(), resultPage.hasNext());
    }


    public Perfume checkConflictPerfume(Long id) {
        if (perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).isEmpty()) {
            throw new CustomException(ResponseCode.PERFUME_NOT_FOUND);
        }
        if (perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).get().getState().equals(PerfumeState.INACTIVE)) {
            throw new CustomException(ResponseCode.PERFUME_DELETED);
        }
        return perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).get();
    }

    public Note checkConflictNote(Long id) {
        if (noteRepository.findNoteById(id).isEmpty()) {
            throw new CustomException(ResponseCode.NOTE_NOT_FOUND);
        } else {
            return noteRepository.findNoteById(id).get();
        }
    }
}

package com.userservice.perfume.service;

import com.userservice.global.CustomException;
import com.userservice.global.ResponseCode;
import com.userservice.global.SearchType;
import com.userservice.perfume.dto.GetNoteListResponseDto;
import com.userservice.perfume.dto.GetNoteResponseDto;
import com.userservice.perfume.dto.GetPerfumeListResponseDto;
import com.userservice.perfume.dto.GetPerfumeResponseDto;
import com.userservice.perfume.entity.Note;
import com.userservice.perfume.entity.Perfume;
import com.userservice.perfume.entity.PerfumeState;
import com.userservice.perfume.repository.NoteRepository;
import com.userservice.perfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfumeServiceImpl implements PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final NoteRepository noteRepository;

    @Override
    @Transactional(readOnly = true)
    public GetNoteResponseDto getNote(Long id) {
        return GetNoteResponseDto.getNote(checkConflictNote(id));
    }

    @Override
    @Transactional(readOnly = true)
    public GetPerfumeResponseDto getPerfume(Long id) {
        return GetPerfumeResponseDto.getPerfume(checkConflictPerfume(id));
    }

    // 전체 리스트
    @Override
    @Transactional(readOnly = true)
    public GetNoteListResponseDto searchNotes(SearchType searchType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        Specification<Note> spec = Specification.where(null);

        if (searchType == SearchType.NAME) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("note_nm"), "%" + keyword + "%"));
        }

        Page<Note> resultPage = noteRepository.findAll(spec, pageable);
        List<GetNoteListResponseDto.NoteListInfo> noteList = resultPage.getContent().stream()
                .map(GetNoteListResponseDto.NoteListInfo::fromNote)
                .toList();

        return GetNoteListResponseDto.createResponse(noteList, resultPage.getTotalPages(), resultPage.hasNext());
    }

    @Override
    @Transactional(readOnly = true)
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

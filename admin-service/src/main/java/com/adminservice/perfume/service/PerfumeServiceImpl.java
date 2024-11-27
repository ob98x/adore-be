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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerfumeServiceImpl implements PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final NoteRepository noteRepository;
    private final FileManager fileManager;

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createNote(NoteCreateRequestDto noteCreateRequestDto) {
        log.info("[Perfume Service - createNote]: 노트를 생성합니다. noteCreateRequestDto: {}", noteCreateRequestDto);

        MultipartFile file = checkValidType(noteCreateRequestDto.getFile());

        String imageUri = "init";
        log.info("[Perfume Service - createNote]: 이미지를 업로드합니다. imageUri: {}", imageUri);
        try {
            imageUri = fileManager.uploadImage(file);
        } catch (Exception e) {
            log.error("[Perfume Service - createNote]: 이미지 업로드에 실패했습니다. e: {}", e.getMessage());
        }
        noteCreateRequestDto.setNoteImg(imageUri);

        log.info("[Perfume Service - createNote]: 노트를 저장합니다. noteCreateRequestDto: {}", noteCreateRequestDto);
        noteRepository.save(NoteCreateRequestDto.createNote(noteCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.NOTE_CREATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateNote(Long id, NoteCreateRequestDto noteCreateRequestDto) {
        log.info("[Perfume Service - updateNote]: 노트를 수정합니다. id: {}, noteCreateRequestDto: {}", id, noteCreateRequestDto);

        MultipartFile file = noteCreateRequestDto.getFile();
        if (file == null) {
            log.info("[Perfume Service - updateNote]: 파일이 없습니다.");
            noteCreateRequestDto.setNoteImg("");
            log.info("[Perfume Service - updateNote]: 이미지를 업로드하지 않습니다.");
        } else {
            checkValidType(file);
            String imageUri = "init";
            log.info("[Perfume Service - updateNote]: 이미지를 업로드합니다. imageUri: {}", imageUri);
            try {
                imageUri = fileManager.uploadImage(file);
            } catch (Exception e) {
                log.error("File upload failed: {}", e.getMessage(), e);
                throw new CustomException(ResponseCode.FILE_NOT_FOUND);
            }
            noteCreateRequestDto.setNoteImg(imageUri);
        }
        noteRepository.save(NoteCreateRequestDto.updateNote(checkConflictNote(id), noteCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.NOTE_UPDATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteNote(Long id) {
        log.info("[Perfume Service - deleteNote]: 노트를 삭제합니다. id: {}", id);

        log.info("[Perfume Service - deleteNote]: 노트 삭제 진행. id: {}", id);

        Note note = checkConflictNote(id);
        noteRepository.delete(note);

        log.info("[Perfume Service - deleteNote]: 노트 삭제 완료. id: {}", id);
        return ResponseEntity.ok(CustomResponseCode.NOTE_DELETE_SUCCESS);
    }


    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createPerfume(PerfumeCreateRequestDto perfumeCreateRequestDto) {
        log.info("[Perfume Service - createPerfume]: 향수를 생성합니다. perfumeCreateRequestDto: {}", perfumeCreateRequestDto);

        MultipartFile file = perfumeCreateRequestDto.getFile();
        if (file == null) {
            log.info("[Perfume Service - createPerfume]: 파일이 없습니다.");
            perfumeCreateRequestDto.setPerfumePhoto("");
            log.info("[Perfume Service - createPerfume]: 이미지를 업로드하지 않습니다.");
        } else {
            checkValidType(file);
            String imageUri = "init";
            log.info("[Perfume Service - createPerfume]: 이미지를 업로드합니다. imageUri: {}", imageUri);
            try {
                imageUri = fileManager.uploadImage(file);
            } catch (Exception e) {
                log.error("File upload failed: {}", e.getMessage(), e);
                throw new CustomException(ResponseCode.FILE_NOT_FOUND);
            }
            perfumeCreateRequestDto.setPerfumePhoto(imageUri);
        }

        log.info("[Perfume Service - createPerfume]: 향수를 저장합니다. perfumeCreateRequestDto: {}", perfumeCreateRequestDto);
        perfumeRepository.save(PerfumeCreateRequestDto.createPerfume(perfumeCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.PERFUME_CREATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updatePerfume(Long id, PerfumeCreateRequestDto perfumeCreateRequestDto) {
        log.info("[Perfume Service - updatePerfume]: 향수를 수정합니다. id: {}", id);

        MultipartFile file = perfumeCreateRequestDto.getFile();
        if (file == null) {
            log.info("[Perfume Service - updatePerfume]: 파일이 없습니다.");
            perfumeCreateRequestDto.setPerfumePhoto("");
            log.info("[Perfume Service - updatePerfume]: 이미지를 업로드하지 않습니다.");
        } else {
            checkValidType(file);
            String imageUri = "init";
            log.info("[Perfume Service - updatePerfume]: 이미지를 업로드합니다. imageUri: {}", imageUri);
            try {
                imageUri = fileManager.uploadImage(file);
            } catch (Exception e) {
                log.error("File upload failed: {}", e.getMessage(), e);
                throw new CustomException(ResponseCode.FILE_NOT_FOUND);
            }
            perfumeCreateRequestDto.setPerfumePhoto(imageUri);
        }
        perfumeRepository.save(PerfumeCreateRequestDto.updatePerfume(checkConflictPerfume(id), perfumeCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.PERFUME_UPDATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deletePerfume(Long id) {
        log.info("[Perfume Service - deletePerfume]: 향수를 삭제합니다. id: {}", id);
        Perfume perfume = checkConflictPerfume(id);

        log.info("[Perfume Service - deletePerfume]: 향수 삭제 진행. id: {}", id);
        perfume.setState(PerfumeState.INACTIVE);
        perfumeRepository.save(perfume);
        log.info("[Perfume Service - deletePerfume]: 향수 삭제 완료. id: {}", id);
        return ResponseEntity.ok(CustomResponseCode.PERFUME_DELETE_SUCCESS);
    }

    @Override
    public Perfume checkConflictPerfume(Long id) {
        log.info("[Review Service - checkConflictPerfume]: id: {}의 향수를 찾습니다.", id);
        if (perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).isEmpty()) {
            log.error("[Review Service - checkConflictPerfume]: 향수를 찾을 수 없습니다.");
            throw new CustomException(ResponseCode.PERFUME_NOT_FOUND);
        }
        if (perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).get().getState().equals(PerfumeState.INACTIVE)) {
            log.error("[Review Service - checkConflictPerfume]: 향수가 삭제되었습니다.");
            throw new CustomException(ResponseCode.PERFUME_DELETED);
        }
        return perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).get();
    }

    @Override
    public Note checkConflictNote(Long id) {
        if (noteRepository.findNoteById(id).isEmpty()) {
            log.info("[Review Service - checkConflictNote]: 노트를 찾을 수 없습니다.");
            throw new CustomException(ResponseCode.NOTE_NOT_FOUND);
        } else {
            return noteRepository.findNoteById(id).get();
        }
    }

    public MultipartFile checkValidType(MultipartFile file) {
        log.info("[Review Service - checkValidType]: 파일이 이미지 형식인지 확인합니다. file: {}", file);
        if (file == null || file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image")) {
            throw new CustomException(ResponseCode.INVALID_FILE_TYPE);
        }
        return file;
    }
}

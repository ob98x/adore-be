package com.adminservice.perfume.dto;

import com.adminservice.perfume.entity.Note;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class NoteCreateRequestDto {

    @Schema(description = "노트 이름", example = "우디")
    private String name;

    @Schema(description = "노트 내용", example = "노트 설명")
    private String content;

    @Schema(description = "부모 노트 ID", example = "1")
    private Long parentNoteId;

    @Schema(description = "노트 이미지", example = "이미지 GCS 경로")
    private MultipartFile file;

    @Schema(description = "노트 이미지", example = "이미지 GCS 경로")
    private String noteImg;

    public static Note createNote(NoteCreateRequestDto noteCreateRequestDto) {
        return Note.builder()
                .noteNm(noteCreateRequestDto.getName())
                .noteContent(noteCreateRequestDto.getContent())
                .parentNoteId(noteCreateRequestDto.getParentNoteId())
                .noteImg(noteCreateRequestDto.getNoteImg())
                .build();
    }

    public static Note updateNote(Note note, NoteCreateRequestDto noteCreateRequestDto) {
        BeanUtils.copyProperties(noteCreateRequestDto, note); // 필요한 경우 비밀번호 제외
        return note;
    }

}

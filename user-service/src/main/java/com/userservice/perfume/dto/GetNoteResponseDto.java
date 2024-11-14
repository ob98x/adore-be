package com.userservice.perfume.dto;


import com.userservice.perfume.entity.Note;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetNoteResponseDto {

    @Schema(description = "노트 ID", example = "1")
    private Long id;

    @Schema(description = "노트 이름", example = "우디")
    private String noteNm;

    @Schema(description = "노트 내용", example = "노트 설명")
    private String noteContent;

    @Schema(description = "노트 이미지", example = "이미지 GCS 경로")
    private String noteImg;

    @Schema(description = "부모 노트 ID", example = "1")
    private Long parentNoteId;

    @Builder
    public GetNoteResponseDto(Long id, String noteNm, String noteContent, String noteImg, Long parentNoteId) {
        this.id = id;
        this.noteNm = noteNm;
        this.noteContent = noteContent;
        this.noteImg = noteImg;
        this.parentNoteId = parentNoteId;
    }

    public static GetNoteResponseDto getNote(Note note) {
        return GetNoteResponseDto.builder()
                .id(note.getId())
                .noteNm(note.getNoteNm())
                .noteContent(note.getNoteContent())
                .noteImg(note.getNoteImg())
                .parentNoteId(note.getParentNoteId())
                .build();
    }
}

package com.userservice.perfume.dto;


import com.userservice.perfume.entity.Note;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetNoteResponseDto {
    private Long id;
    private String noteNm;
    private String noteContent;
    private String noteImg;
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

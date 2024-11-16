package com.userservice.perfume.dto;

import com.userservice.perfume.entity.Note;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetParentNoteResponseDto {
    private Long id;
    private String noteNm;
    private String noteImg

    @Builder
    public GetParentNoteResponseDto(Long id, String noteNm, String noteImg) {
        this.id = id;
        this.noteNm = noteNm;
        this.noteImg = noteImg;
    }

    public static List<GetParentNoteResponseDto> fromNoteList(List<Note> noteList) {
        return noteList.stream()
                .map(note -> GetParentNoteResponseDto.builder()
                        .id(note.getId())
                        .noteNm(note.getNoteNm())
                        .noteImg(note.getNoteImg())
                        .build())
                .toList();
    }

}

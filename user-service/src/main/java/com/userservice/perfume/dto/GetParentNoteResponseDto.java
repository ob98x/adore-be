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

    @Builder
    public GetParentNoteResponseDto(Long id, String noteNm) {
        this.id = id;
        this.noteNm = noteNm;
    }

    public static List<GetParentNoteResponseDto> fromNoteList(List<Note> noteList) {
        return noteList.stream()
                .map(note -> GetParentNoteResponseDto.builder()
                        .id(note.getId())
                        .noteNm(note.getNoteNm())
                        .build())
                .toList();
    }

}

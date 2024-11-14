package com.userservice.perfume.dto;


import com.userservice.perfume.entity.Note;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetNoteListResponseDto {

    @Schema(description = "노트 목록", example = "[{\"id\": 1, \"noteNm\": \"우디\", \"noteContent\": \"노트 설명\", \"noteImg\": \"이미지 GCS 경로\", \"parentNoteId\": 1}]")
    private List<NoteListInfo> noteList;
    
    @Schema(description = "총 페이지 수", example = "1")
    private int totalPages;
    
    @Schema(description = "다음 페이지 존재 여부", example = "false")
    private boolean hasNext;


    @Setter
    @Getter
    public static class NoteListInfo {
        
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

        public static NoteListInfo fromNote(Note note) {
            NoteListInfo noteList = new NoteListInfo();
            noteList.setId(note.getId());
            noteList.setNoteNm(note.getNoteNm());
            noteList.setNoteContent(note.getNoteContent());
            noteList.setNoteImg(note.getNoteImg());
            noteList.setParentNoteId(note.getParentNoteId());
            return noteList;
        }
    }
    public static GetNoteListResponseDto createResponse(List<NoteListInfo> noteList, int totalPages, boolean hasNext) {
        GetNoteListResponseDto response = new GetNoteListResponseDto();
        response.setNoteList(noteList);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }
}

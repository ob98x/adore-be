package com.adminservice.perfume.dto;


import com.adminservice.perfume.entity.Note;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetNoteListResponseDto {
    private List<NoteListInfo> noteList;
    private int totalPages;
    private boolean hasNext;


    @Setter
    @Getter
    public static class NoteListInfo {
        private Long id;
        private String noteNm;
        private String noteContent;
        private String noteImg;
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

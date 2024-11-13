package com.adminservice.perfume.dto;

import com.adminservice.perfume.entity.Note;
import com.adminservice.perfume.entity.Perfume;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class NoteCreateRequestDto {
    private String name;
    private String content;
    private Long parentNoteId;
    private MultipartFile file;
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

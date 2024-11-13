package com.adminservice.perfume.entity;

import com.adminservice.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "note")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Note extends BaseEntity {

    @Column(name = "notm_nm")
    private String noteNm;

    @Column(name = "note_content", nullable = false)
    private String noteContent;

    @Column(name = "note_img", nullable = false)
    private String noteImg;

    @Column(name = "parent_note_id", nullable = false)
    private Long parentNoteId;

    @Builder
    public Note(String noteNm, String noteContent, String noteImg, Long parentNoteId) {
        this.noteNm = noteNm;
        this.noteContent = noteContent;
        this.noteImg = noteImg;
        this.parentNoteId = parentNoteId;
    }

    public static Note of(String noteNm, String noteContent, String noteImg, Long parentNoteId) {
        return Note.builder()
                .noteNm(noteNm)
                .noteContent(noteContent)
                .noteImg(noteImg)
                .parentNoteId(parentNoteId)
                .build();
    }
}

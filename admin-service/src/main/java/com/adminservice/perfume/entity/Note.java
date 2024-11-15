package com.adminservice.perfume.entity;

import com.adminservice.global.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "note")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(name = "Note", description = "노트의 객체")
public class Note extends BaseEntity {

    @Schema(description = "노트 이름", example = "우디")
    @Column(name = "notm_nm", nullable = false)
    private String noteNm;

    @Schema(description = "노트 내용", example = "노트 설명")
    @Column(name = "note_content", nullable = false)
    private String noteContent;

    @Schema(description = "노트 이미지", example = "이미지 GCS 경로")
    @Column(name = "note_img", nullable = false)
    private String noteImg;

    @Schema(description = "부모 노트 ID", example = "1")
    @Column(name = "parent_note_id", nullable = false)
    private Long parentNoteId;

    @Builder
    public Note (String noteNm, String noteContent, String noteImg, Long parentNoteId) {
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

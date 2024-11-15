package com.adminservice.question.entity;

import com.adminservice.global.BaseEntity;
import com.adminservice.user.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question")
@Schema(name = "Question", description = "문의의 객체")
public class Question extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Schema(description = "문의자", example = "1")
    private Member applicant;

    @OneToOne
    @JoinColumn(name="processor_id")
    @Schema(description = "처리자", example = "1")
    private Member processor;

    @Column(name = "title", nullable = false)
    @Schema(description = "문의 제목", example = "문의 제목")
    private String title;

    @Column(name = "content", nullable = false)
    @Schema(description = "문의 내용", example = "문의 내용")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    @Schema(description = "문의 카테고리", example = "PERFUME")
    private QuestionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_state", nullable = false)
    @Schema(description = "문의 상태", example = "WAIT")
    private QuestionState state;

    @Column(name = "answer_content")
    @Schema(description = "답변 내용", example = "답변 내용")
    private String answerContent;

}
package com.adminservice.question.dto;

import com.adminservice.question.entity.Question;
import com.adminservice.question.entity.QuestionState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

public class GetQuestionResponseDto {
    @Schema(description = "문의 ID", example = "1")
    private Long id;

    @Schema(description = "문의 제목", example = "문의 제목")
    private String title;

    @Schema(description = "문의 내용", example = "문의 내용")
    private String content;

    @Schema(description = "문의자 이름", example = "문의자 이름")
    private String writerName;

    @Schema(description = "문의자 이메일", example = "문의자 이메일")
    private String writerEmail;

    @Schema(description = "문의자 ID", example = "1")
    private Long writerId;

    @Schema(description = "처리자 이름", example = "처리자 이름")
    private String processorName;

    @Schema(description = "처리자 이메일", example = "처리자 이메일")
    private String processorEmail;

    @Schema(description = "처리자 ID", example = "1")
    private Long processorId;

    @Schema(description = "문의 카테고리", example = "PERFUME")
    private String category;

    @Schema(description = "문의 상태", example = "WAIT")
    private QuestionState state;

    @Schema(description = "생성일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "수정일", example = "2021-07-01T00:00:00")
    private LocalDateTime updatedDate;

    @Schema(description = "답변 내용", example = "답변 내용")
    private String answerContent;

    @Schema(description = "처리 가능 여부", example = "true")
    private boolean canProcess;

    @Builder
    private GetQuestionResponseDto(Long id, String title, String content, String writerName, String writerEmail, Long writerId, String processorName, String processorEmail, Long processorId, String category, QuestionState state, LocalDateTime createdDate, LocalDateTime updatedDate, String answerContent, boolean canProcess) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.writerEmail = writerEmail;
        this.writerId = writerId;
        this.processorName = processorName;
        this.processorEmail = processorEmail;
        this.processorId = processorId;
        this.category = category;
        this.state = state;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.answerContent = answerContent;
        this.canProcess = canProcess;
    }

    public static GetQuestionResponseDto createResponse(Question question) {
        return GetQuestionResponseDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .writerName(question.getApplicant().getName())
                .writerEmail(question.getApplicant().getEmail())
                .writerId(question.getApplicant().getId())
                .processorName(question.getProcessor().getName())
                .processorEmail(question.getProcessor().getEmail())
                .processorId(question.getProcessor().getId())
                .category(question.getCategory().name())
                .state(question.getState())
                .createdDate(question.getCreatedAt())
                .updatedDate(question.getUpdatedAt())
                .answerContent(question.getAnswerContent())
                .canProcess(question.getState() == QuestionState.WAIT)
                .build();
    }
}

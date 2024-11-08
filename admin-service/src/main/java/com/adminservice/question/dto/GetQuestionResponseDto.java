package com.adminservice.question.dto;

import com.adminservice.question.entity.Question;
import com.adminservice.question.entity.QuestionState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

public class GetQuestionResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writerName;
    private String writerEmail;
    private Long writerId;
    private String processorName;
    private String processorEmail;
    private Long processorId;
    private String category;
    private QuestionState state;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String answerContent;
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

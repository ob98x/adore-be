package com.userservice.review.dto;

import com.userservice.review.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    @Schema(description = "댓글 ID", example = "1")
    private Long id;

    @Schema(description = "댓글 내용", example = "댓글 내용")
    private String content;

    @Schema(description = "댓글 상태", example = "ACTIVE / INACTIVE")
    private String state;

    @Schema(description = "작성자 ID", example = "1")
    private Long writerId;

    @Schema(description = "생성일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일", example = "2021-07-01T00:00:00")
    private LocalDateTime updatedAt;

    @Builder
    public CommentDto(Long id, String content, String state, Long writerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.state = state;
        this.writerId = writerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .state(comment.getState().name())
                .writerId(comment.getWriter().getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}

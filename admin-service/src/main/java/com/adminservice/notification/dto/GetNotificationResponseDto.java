package com.adminservice.notification.dto;

import com.adminservice.notification.entity.Notification;
import com.adminservice.notification.entity.NotificationState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

public class GetNotificationResponseDto {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "공지사항입니다.")
    private String title;

    @Schema(description = "내용", example = "공지사항입니다.")
    private String content;

    @Schema(description = "작성자 이름", example = "admin")
    private String writerName;

    @Schema(description = "상태", example = "ACTIVE")
    private NotificationState state;

    @Schema(description = "생성일", example = "2021-07-01T00:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "수정일", example = "2021-07-01T00:00:00")
    private LocalDateTime updatedDate;

    @Builder
    private GetNotificationResponseDto(Long id, String title, String content, String writerName, NotificationState state, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.state = state;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public static GetNotificationResponseDto createResponse(Notification notification) {
        return GetNotificationResponseDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .writerName(notification.getMember().getName())
                .state(notification.getState())
                .createdDate(notification.getCreatedAt())
                .updatedDate(notification.getUpdatedAt())
                .build();
    }
}

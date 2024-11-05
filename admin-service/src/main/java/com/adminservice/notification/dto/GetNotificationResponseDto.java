package com.adminservice.notification.dto;

import com.adminservice.notification.entity.Notification;
import com.adminservice.notification.entity.NotificationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder

public class GetNotificationResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writerName;
    private String writerEmail;
    private Long writerId;
    private NotificationState state;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    private GetNotificationResponseDto(Long id, String title, String content, String writerName, String writerEmail, Long writerId, NotificationState state, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.writerEmail = writerEmail;
        this.writerId = writerId;
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
                .writerEmail(notification.getMember().getEmail())
                .writerId(notification.getMember().getId())
                .state(notification.getState())
                .createdDate(notification.getCreatedAt())
                .updatedDate(notification.getUpdatedAt())
                .build();
    }
}

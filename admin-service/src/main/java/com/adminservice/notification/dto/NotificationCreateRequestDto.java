package com.adminservice.notification.dto;

import com.adminservice.notification.entity.Notification;
import com.adminservice.notification.entity.NotificationState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class NotificationCreateRequestDto {

    private String title;
    private String content;

    public static Notification createNotification(NotificationCreateRequestDto notificationCreateRequestDto) {
        return Notification.builder()
                .title(notificationCreateRequestDto.getTitle())
                .content(notificationCreateRequestDto.getContent())
                .state(NotificationState.ACTIVE)
                .build();
    }

    public static Notification updateNotification(Notification notification, NotificationCreateRequestDto notificationCreateRequestDto) {
        BeanUtils.copyProperties(notificationCreateRequestDto, notification);
        return notification;
    }
}
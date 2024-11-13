package com.adminservice.notification.dto;

import com.adminservice.notification.entity.Notification;
import com.adminservice.notification.entity.NotificationState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class NotificationCreateRequestDto {

    private String title;
    private String content;
    private Long memberId;

    public static Notification createNotification(NotificationCreateRequestDto notificationCreateRequestDto) {
        return Notification.builder()
                .title(notificationCreateRequestDto.getTitle())
                .content(notificationCreateRequestDto.getContent())
                .state(NotificationState.ACTIVE)
                .build();
    }
}
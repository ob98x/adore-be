package com.adminservice.notification.dto;

import com.adminservice.notification.entity.Notification;
import com.adminservice.notification.entity.NotificationState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Not;
import org.checkerframework.checker.units.qual.N;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class NotificationCreateRequestDto {

    @NotBlank
    @Schema(description = "제목", example = "공지사항입니다.")
    private String title;

    @NotBlank
    @Schema(description = "내용", example = "공지사항입니다.")
    private String content;

    public static Notification updateNotification(Notification notification, NotificationCreateRequestDto notificationCreateRequestDto) {
        BeanUtils.copyProperties(notificationCreateRequestDto, notification);
        return notification;
    }
}
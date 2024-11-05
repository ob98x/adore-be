package com.adminservice.notification.dto;

import com.adminservice.notification.entity.Notification;
import com.adminservice.notification.entity.NotificationState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetNotificationListResponseDto {

    private List<NotificationListInfo> notificationList;
    private int totalPages;
    private boolean hasNext;

    @Getter
    @Setter
    public static class NotificationListInfo {
        private String title;
        private String nickname;
        private String email;
        private NotificationState state;
        private LocalDateTime createdAt;

        // 정적 팩토리 메서드를 추가하여 변환 간소화
        public static NotificationListInfo fromNotification(Notification notification) {
            NotificationListInfo info = new NotificationListInfo();
            info.setTitle(notification.getTitle());
            info.setNickname(notification.getMember().getNickname());
            info.setEmail(notification.getMember().getEmail());
            info.setState(notification.getState());
            info.setCreatedAt(notification.getCreatedAt());
            return info;
        }
    }
    public static GetNotificationListResponseDto createResponse(List<NotificationListInfo> notificationList, int totalPages, boolean hasNext) {
        GetNotificationListResponseDto response = new GetNotificationListResponseDto();
        response.setNotificationList(notificationList);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }
}

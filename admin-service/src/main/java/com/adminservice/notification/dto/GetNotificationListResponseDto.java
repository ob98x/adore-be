package com.adminservice.notification.dto;

import com.adminservice.notification.entity.Notification;
import com.adminservice.notification.entity.NotificationState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetNotificationListResponseDto {

    @Schema(description = "공지사항 리스트", example = " \"title\": \"공지사항입니다.\", \"nickname\": \"admin\", \"email\": \"dyw1014@gachon.ac.kr\", \"state\": \"ACTIVE\", \"createdAt\": \"2021-07-01T00:00:00\"")
    private List<NotificationListInfo> notificationList;

    @Schema(description = "총 페이지 수", example = "1")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;

    @Getter
    @Setter
    public static class NotificationListInfo {

        @Schema(description = "제목", example = "공지사항입니다.")
        private String title;

        @Schema(description = "닉네임", example = "admin")
        private String nickname;

        @Schema(description = "이메일", example = "dyw1014@gachon.ac.kr")
        private String email;

        @Schema(description = "상태", example = "ACTIVE")
        private NotificationState state;

        @Schema(description = "생성일", example = "2021-07-01T00:00:00")
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

package com.adminservice.notification.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.notification.dto.GetNotificationListResponseDto;
import com.adminservice.notification.dto.GetNotificationResponseDto;
import com.adminservice.notification.dto.NotificationCreateRequestDto;
import com.adminservice.notification.entity.Notification;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    ResponseEntity<CustomResponseCode> createNotification(String authorization, NotificationCreateRequestDto notificationCreateRequestDto);
    ResponseEntity<CustomResponseCode> updateNotification(String authorization, Long id, NotificationCreateRequestDto notificationCreateRequestDto);
    ResponseEntity<CustomResponseCode> deleteNotification(String authorization, Long id);
    ResponseEntity<GetNotificationResponseDto> getNotification(Long id);
    GetNotificationListResponseDto getNotificationLists(SearchType searchType, String keyword, int page);
    Notification checkConflictNotification(Long id);
}

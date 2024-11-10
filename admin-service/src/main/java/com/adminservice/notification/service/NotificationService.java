package com.adminservice.notification.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.notification.dto.GetNotificationListResponseDto;
import com.adminservice.notification.dto.GetNotificationResponseDto;
import com.adminservice.notification.dto.NotificationCreateRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NotificationService {
    ResponseEntity<GetNotificationResponseDto> getNotification(Long id);
    GetNotificationListResponseDto getNotificationLists(SearchType searchType, String keyword, int page);
    List<GetNotificationListResponseDto.NotificationListInfo> allNotifications();
    ResponseEntity<CustomResponseCode> createNotification(NotificationCreateRequestDto notificationCreateRequestDto);
    ResponseEntity<CustomResponseCode> updateNotification(Long id, NotificationCreateRequestDto notificationCreateRequestDto);
    ResponseEntity<CustomResponseCode> deleteNotification(Long id);
}

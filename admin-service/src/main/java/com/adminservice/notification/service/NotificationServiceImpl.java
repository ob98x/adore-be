package com.adminservice.notification.service;

import com.adminservice.global.CustomException;
import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.ResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.notification.dto.GetNotificationListResponseDto;
import com.adminservice.notification.dto.GetNotificationResponseDto;
import com.adminservice.notification.dto.NotificationCreateRequestDto;
import com.adminservice.notification.entity.Notification;
import com.adminservice.notification.entity.NotificationState;
import com.adminservice.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public ResponseEntity<CustomResponseCode> createNotification(NotificationCreateRequestDto notificationCreateRequestDto) {
        notificationRepository.save(NotificationCreateRequestDto.createNotification(notificationCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.NOTIFICATION_CREATE_SUCCESS);
    }

    public ResponseEntity<CustomResponseCode> updateNotification(Long id, NotificationCreateRequestDto notificationCreateRequestDto) {
        notificationRepository.save(NotificationCreateRequestDto.updateNotification(checkConflictNotification(id), notificationCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.NOTIFICATION_UPDATE_SUCCESS);
    }

    public ResponseEntity<CustomResponseCode> deleteNotification(Long id){
        return ResponseEntity.ok(CustomResponseCode.NOTIFICATION_DELETE_SUCCESS);
    }

    @Override
    public ResponseEntity<GetNotificationResponseDto> getNotification(Long id) {
        return ResponseEntity.ok(GetNotificationResponseDto.createResponse(checkConflictNotification(id)));
    }

    @Override
    public GetNotificationListResponseDto getNotificationLists(SearchType searchType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Specification<Notification> spec = Specification.where(null);

        // 검색 조건 추가
        if (searchType != null) {
            if (searchType == SearchType.TITLE) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("title"), "%" + keyword + "%"));
            }
        }

        Page<Notification> resultPage = notificationRepository.findAll(spec, pageable);
        List<GetNotificationListResponseDto.NotificationListInfo> notificationList = resultPage.getContent().stream()
                .map(GetNotificationListResponseDto.NotificationListInfo::fromNotification)
                .toList();

        return GetNotificationListResponseDto.createResponse(notificationList, resultPage.getTotalPages(), resultPage.hasNext());
    }


    public Notification checkConflictNotification(Long id) {
        // Check if the question exists
        if (notificationRepository.findNotificationById(id).isEmpty()) {
            throw new CustomException(ResponseCode.NOTIFICATION_NOT_FOUND);
        }

        // Check if the question is inactive
        if (notificationRepository.findNotificationById(id).get().getState().equals(NotificationState.INACTIVE)) {
            throw new CustomException(ResponseCode.NOTIFICATION_DELETED);
        }
        return notificationRepository.findNotificationById(id).get();
    }

}

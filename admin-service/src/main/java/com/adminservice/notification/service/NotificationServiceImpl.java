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
import com.adminservice.user.entity.Member;
import com.adminservice.user.repository.MemberRepository;
import com.adminservice.user.service.MemberService;
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
    private final MemberRepository memberRepository;

    public ResponseEntity<CustomResponseCode> createNotification(NotificationCreateRequestDto notificationCreateRequestDto) {
        Member member = memberRepository.findById(notificationCreateRequestDto.getMemberId()).orElseThrow(
                () -> new CustomException(ResponseCode.MEMBER_NOT_FOUND)
        );

        Notification notifications = Notification.builder()
                .title(notificationCreateRequestDto.getTitle())
                .content(notificationCreateRequestDto.getContent())
                .member(member)
                .state(NotificationState.ACTIVE)
                .build();
        notificationRepository.save(notifications);
        return ResponseEntity.ok(CustomResponseCode.NOTIFICATION_CREATE_SUCCESS);
    }

    public ResponseEntity<CustomResponseCode> updateNotification(Long id, NotificationCreateRequestDto notificationCreateRequestDto) {
        Notification notification = checkConflictNotification(id);
        Member member = memberRepository.findById(notificationCreateRequestDto.getMemberId()).orElseThrow(
                () -> new CustomException(ResponseCode.MEMBER_NOT_FOUND)
        );

        notification.setContent(notificationCreateRequestDto.getContent());
        notification.setTitle(notificationCreateRequestDto.getTitle());
        notification.setMember(member);

        notificationRepository.save(notification);

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
    public List<GetNotificationListResponseDto.NotificationListInfo> allNotifications() {
        List<Notification> notificationList = notificationRepository.findAll();
        return notificationList.stream()
                .map(GetNotificationListResponseDto.NotificationListInfo::fromNotification)
                .toList();
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

package com.adminservice.notification.service;

import com.adminservice.global.*;
import com.adminservice.notification.dto.GetNotificationListResponseDto;
import com.adminservice.notification.dto.GetNotificationResponseDto;
import com.adminservice.notification.dto.NotificationCreateRequestDto;
import com.adminservice.notification.entity.Notification;
import com.adminservice.notification.entity.NotificationState;
import com.adminservice.notification.repository.NotificationRepository;
import com.adminservice.perfume.entity.PerfumeState;
import com.adminservice.user.entity.Member;
import com.adminservice.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberService memberService;
    private final FeignUtil feignUtil;

    public ResponseEntity<CustomResponseCode> createNotification(String authorization, NotificationCreateRequestDto notificationCreateRequestDto) {
        log.info("[ Notification Service - createNotification ] - 공지사항 생성 요청이 들어왔습니다.");

        Long memberId = feignUtil.getTokenInfo(authorization).getMemberId();
        Member member = memberService.checkConflictMember(memberId);
        log.info("[ Notification Service - createNotification ] - memberId: {}", memberId);

        Notification notifications = Notification.builder()
                .title(notificationCreateRequestDto.getTitle())
                .content(notificationCreateRequestDto.getContent())
                .member(member)
                .state(NotificationState.ACTIVE)
                .build();

        log.info("[ Notification Service - createNotification ] - 공지사항 생성 완료");
        notificationRepository.save(notifications);
        return ResponseEntity.ok(CustomResponseCode.NOTIFICATION_CREATE_SUCCESS);
    }

    public ResponseEntity<CustomResponseCode> updateNotification(String authorization, Long id, NotificationCreateRequestDto notificationCreateRequestDto) {
        log.info("[ Notification Service - updateNotification ] - 공지사항 수정 요청이 들어왔습니다., id: {}", id);

        Long memberId = feignUtil.getTokenInfo(authorization).getMemberId();
        Member member = memberService.checkConflictMember(memberId);
        log.info("[ Notification Service - updateNotification ] - memberId: {}", memberId);

        memberService.checkAuthorizeMember(member.getId(), member.getId());

        Notification notification = checkConflictNotification(id);
        log.info("[ Notification Service - updateNotification ] - 공지사항 정보 조회 완료");

        notification.setContent(notificationCreateRequestDto.getContent());
        notification.setTitle(notificationCreateRequestDto.getTitle());
        notification.setMember(member);
        log.info("[ Notification Service - updateNotification ] - 공지사항 수정 완료");

        notificationRepository.save(notification);

        return ResponseEntity.ok(CustomResponseCode.NOTIFICATION_UPDATE_SUCCESS);
    }

    public ResponseEntity<CustomResponseCode> deleteNotification(String authorization, Long id){
        log.info("[ Notification Service - deleteNotification ] - 공지사항 삭제 요청이 들어왔습니다., id: {}", id);

        Long memberId = feignUtil.getTokenInfo(authorization).getMemberId();
        Member member = memberService.checkConflictMember(memberId);
        log.info("[ Notification Service - deleteNotification ] - memberId: {}", memberId);

        memberService.checkAuthorizeMember(member.getId(), member.getId());

        Notification notification = checkConflictNotification(id);

        notification.setState(NotificationState.INACTIVE);
        notificationRepository.save(notification);
        log.info("[ Notification Service - deleteNotification ] - 공지사항 삭제 완료");

        return ResponseEntity.ok(CustomResponseCode.NOTIFICATION_DELETE_SUCCESS);
    }

    @Override
    public ResponseEntity<GetNotificationResponseDto> getNotification(Long id) {
        log.info("[ Notification Service - getNotification ] - 공지사항 정보 조회 요청이 들어왔습니다., id: {}", id);
        return ResponseEntity.ok(GetNotificationResponseDto.createResponse(checkConflictNotification(id)));
    }


    @Override
    public GetNotificationListResponseDto getNotificationLists(SearchType searchType, String keyword, int page) {
        log.info("[ Notification Service - getNotificationLists ] - 공지사항 리스트 조회 요청이 들어왔습니다.");

        Pageable pageable = PageRequest.of(page, 10);

        log.info("[ Notification Service - getNotificationLists ] - 검색 조건을 설정합니다.");

        Specification<Notification> spec = Specification.where(null);
        if ((keyword.isEmpty())) {
            spec = spec.and((root, query, cb) ->
                    cb.notEqual(root.get("state"), NotificationState.ACTIVE));
        } else {
            if (searchType != null) {
                if (searchType == SearchType.TITLE) {
                    spec = spec.and((root, query, cb) ->
                            cb.like(root.get("title"), "%" + keyword + "%"));
                }
                else {
                    spec = spec.and((root, query, cb) ->
                            cb.equal(root.get("state"), PerfumeState.ACTIVE));
                }
            }
        }

        log.info("[ Notification Service - getNotificationLists ] - 공지사항 리스트를 DB 에서 가져옵니다.");
        Page<Notification> resultPage = notificationRepository.findAll(spec, pageable);

        log.info("[ Notification Service - getNotificationLists ] - 공지사항 리스트를 DTO 로 변환합니다.");
        List<GetNotificationListResponseDto.NotificationListInfo> notificationList = resultPage.getContent().stream()
                .map(GetNotificationListResponseDto.NotificationListInfo::fromNotification)
                .toList();

        log.info("[ Notification Service - getNotificationLists ] - 공지사항 리스트 조회 완료");
        return GetNotificationListResponseDto.createResponse(notificationList, resultPage.getTotalPages(), resultPage.hasNext());
    }


    @Override
    public Notification checkConflictNotification(Long id) {
        log.info("[ Notification Service - checkConflictNotification ] - 공지사항 정보 조회, id: {}", id);
        if (notificationRepository.findNotificationById(id).isEmpty()) {
            log.error("[ Notification Service - checkConflictNotification ] - 공지사항을 찾을 수 없습니다., id: {}", id);
            throw new CustomException(ResponseCode.NOTIFICATION_NOT_FOUND);
        }
        if (notificationRepository.findNotificationById(id).get().getState().equals(NotificationState.INACTIVE)) {
            log.error("[ Notification Service - checkConflictNotification ] - 삭제된 공지사항입니다., id: {}", id);
            throw new CustomException(ResponseCode.NOTIFICATION_DELETED);
        }
        return notificationRepository.findNotificationById(id).get();
    }



}

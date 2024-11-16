package com.adminservice.notification.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.notification.dto.GetNotificationListResponseDto;
import com.adminservice.notification.dto.GetNotificationResponseDto;
import com.adminservice.notification.dto.NotificationCreateRequestDto;
import com.adminservice.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[관리자] 공지 관련 API", description = "Notification API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notification")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "공지 사항 생성 API", description = "공지 사항을 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<CustomResponseCode> createNotification(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody NotificationCreateRequestDto notificationCreateRequestDto) {
        log.info("[ admin service - Notification Controller ]: 공지사항 생성 요청이 들어왔습니다.");
        return notificationService.createNotification(authorization, notificationCreateRequestDto);
    }

    @Operation(summary = "공지 사항 수정 API", description = "공지 사항을 수정합니다.")
    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updateNotification(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody NotificationCreateRequestDto notificationCreateRequestDto,@Parameter(description = "수정할 공지사항 id")  @RequestParam Long id) {
        log.info("[ admin service - Notification Controller ]: 공지사항 수정 요청이 들어왔습니다.");
        return notificationService.updateNotification(authorization, id, notificationCreateRequestDto);
    }

    @Operation(summary = "공지 사항 조회 API", description = "공지 사항을 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetNotificationResponseDto> viewNotification(@Parameter(description = "조회할 공지사항 id") @RequestParam Long id) {
        log.info("[ admin service - Notification Controller ]: 공지사항 조회 요청이 들어왔습니다.");
        return notificationService.getNotification(id);
    }

    @Operation(summary = "공지 사항 삭제 API", description = "공지 사항을 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteMember(
            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "삭제할 공지사항 id") @RequestParam Long id) {
        log.info("[ admin service - Notification Controller ]: 공지사항 삭제 요청이 들어왔습니다.");
        return notificationService.deleteNotification(authorization, id);
    }

    @Operation(summary = "공지 사항 리스트 검색 API", description = "공지 사항 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetNotificationListResponseDto> getNotificationLists(
            @PathVariable("page") int page,
            @RequestParam("searchType") SearchType searchType,
            @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            log.info("[Perfume Controller - searchNotes]: 검색 키워드가 제공되지 않았습니다. 전체 리스트를 반환합니다.");
            keyword = ""; // 빈 문자열 또는 서비스 로직에서 null을 처리
        }
        log.info("[ admin service - Notification Controller ]: 공지사항 리스트 조회 요청이 들어왔습니다.");
        GetNotificationListResponseDto response = notificationService.getNotificationLists(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }
}

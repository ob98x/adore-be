package com.adminservice.notification.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.notification.dto.GetNotificationListResponseDto;
import com.adminservice.notification.dto.GetNotificationResponseDto;
import com.adminservice.notification.dto.NotificationCreateRequestDto;
import com.adminservice.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[관리자] 공지 관련 API", description = "Notification API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "공지 사항 생성 API", description = "공지 사항을 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<CustomResponseCode> createNotification(
            @Valid @RequestBody NotificationCreateRequestDto notificationCreateRequestDto) {
        return notificationService.createNotification(notificationCreateRequestDto);
    }

    @Operation(summary = "공지 사항 수정 API", description = "공지 사항을 수정합니다.")
    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updateNotification(
            @Valid @RequestBody NotificationCreateRequestDto notificationCreateRequestDto,@Parameter(description = "수정할 공지사항 id")  @RequestParam Long id) {
        return notificationService.updateNotification(id, notificationCreateRequestDto);
    }

    @Operation(summary = "공지 사항 조회 API", description = "공지 사항을 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetNotificationResponseDto> viewMemberInfo(@Parameter(description = "조회할 공지사항 id") @RequestParam Long id) {
        return notificationService.getNotification(id);
    }

    @Operation(summary = "공지 사항 삭제 API", description = "공지 사항을 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteMember(@Parameter(description = "삭제할 공지사항 id") @RequestParam Long id) {
        return notificationService.deleteNotification(id);
    }

    @Operation(summary = "[미사용] 공지 사항 리스트 검색 API", description = "공지 사항 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetNotificationListResponseDto> getNotificationLists(
            @PathVariable("page") int page,
            @RequestParam("searchType") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetNotificationListResponseDto response = notificationService.getNotificationLists(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "공지 사항 리스트 조회 API", description = "공지 사항 리스트를 조회합니다.")
    @GetMapping("/list/")
    public ResponseEntity<List<GetNotificationListResponseDto.NotificationListInfo>> getNotificationLists() {
        List<GetNotificationListResponseDto.NotificationListInfo> response = notificationService.allNotifications();
        return ResponseEntity.ok(response);
    }
}

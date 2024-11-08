package com.adminservice.notification.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.notification.dto.GetNotificationListResponseDto;
import com.adminservice.notification.dto.GetNotificationResponseDto;
import com.adminservice.notification.dto.NotificationCreateRequestDto;
import com.adminservice.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updateNotification(
            @Valid @RequestBody NotificationCreateRequestDto notificationCreateRequestDto, @RequestParam Long id) {
        return notificationService.updateNotification(id, notificationCreateRequestDto);
    }

    @GetMapping("/")
    public ResponseEntity<GetNotificationResponseDto> viewMemberInfo(@RequestParam Long id) {
        return notificationService.getNotification(id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteMember(@RequestParam Long id) {
        return notificationService.deleteNotification(id);
    }

    @GetMapping("/lists/{page}")
    public ResponseEntity<GetNotificationListResponseDto> getNotificationLists(
            @PathVariable("page") int page,
            @RequestParam("searchType") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetNotificationListResponseDto response = notificationService.getNotificationLists(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }
}

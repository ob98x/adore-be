package com.userservice.user.controller;

import com.userservice.global.CustomResponseCode;
import com.userservice.user.dto.GetMyPageResponseDto;
import com.userservice.user.dto.UpdateMyPageRequestDto;
import com.userservice.user.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[사용자] 사용자 관련 API", description = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/my")
public class UserController {

    private MemberService memberService;

    @Operation(summary = "마이페이지 조회 API", description = "마이페이지를 조회합니다.")
    @GetMapping("/{id}")
    ResponseEntity<GetMyPageResponseDto> getMyPage(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMyPage(id));
    }

    @Operation(summary = "마이페이지 수정 API", description = "마이페이지를 수정합니다.")
    @PatchMapping("/{id}")
    ResponseEntity<CustomResponseCode> updateNickname(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMyPageRequestDto updateMyPageRequestDto) {
        return memberService.updateMyPage(id, updateMyPageRequestDto);
    }




}

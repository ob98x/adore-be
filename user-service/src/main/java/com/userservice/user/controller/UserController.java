package com.userservice.user.controller;

import com.userservice.global.CustomResponseCode;
import com.userservice.user.dto.GetMyPageResponseDto;
import com.userservice.user.dto.UpdateMyPageRequestDto;
import com.userservice.user.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/my")
public class UserController {

    private MemberService memberService;

    @GetMapping("/{id}")
    ResponseEntity<GetMyPageResponseDto> getMyPage(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMyPage(id));
    }

    @PatchMapping("/{id}")
    ResponseEntity<CustomResponseCode> updateNickname(
            @PathVariable Long id,
            @RequestParam String updateClass,
            @RequestBody @Valid UpdateMyPageRequestDto updateMyPageRequestDto) {
        return memberService.updateMyPage(id, updateMyPageRequestDto);
    }




}

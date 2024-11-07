package com.userservice.user.controller;

import com.userservice.global.CustomResponseCode;
import com.userservice.global.CustomResponseCode;
import com.userservice.user.dto.GetMyPageResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.userservice.user.service.MemberSearchService;
import com.userservice.user.dto.GetMyPageResponseDto;
import com.userservice.user.dto.UpdateMyPageRequestDto;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/my")
public class UserController {

    @GetMapping("/{id}")
    ResponseEntity<GetMyPageResponseDto> getMyPage(@PathVariable Long id) {
        return null;
    }

    @PatchMapping("/{id}")
    ResponseEntity<CustomResponseCode> updateNickname(
            @PathVariable Long id,
            @RequestParam String updateClass,
            @RequestBody @Valid GetMyPageResponseDto getMyPageResponseDto) {
        return null;
    }




}

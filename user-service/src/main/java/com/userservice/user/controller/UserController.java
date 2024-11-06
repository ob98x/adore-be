package com.userservice.user.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.user.dto.GetMemberListResponseDto;
import com.adminservice.user.dto.GetMemberResponseDto;
import com.adminservice.user.dto.MemberCreateRequestDto;
import com.adminservice.user.service.MemberSearchService;
import com.userservice.global.CustomResponseCode;
import com.userservice.user.dto.GetMyPageResponseDto;
import com.userservice.user.entity.UpdateClass;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
            @RequestParam UpdateClass updateClass,
            @RequestBody @Valid GetMyPageResponseDto getMyPageResponseDto) {
        return null;
    }




}

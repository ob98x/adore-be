package com.userservice.user.controller;

import com.userservice.global.CustomResponseCode;
import com.userservice.user.dto.GetMyPageResponseDto;
import com.userservice.user.dto.QuestionCreateRequestDto;
import com.userservice.user.dto.UpdateMyPageRequestDto;
import com.userservice.user.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[사용자] 사용자 관련 API", description = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/my")
@Slf4j
public class UserController {

    private final MemberService memberService;

    @Operation(summary = "마이페이지 조회 API", description = "마이페이지를 조회합니다.")
    @GetMapping("/{id}")
    ResponseEntity<GetMyPageResponseDto> getMyPage(@PathVariable Long id) {
        log.info("[User Controller - getMyPage]: {}번 사용자의 마이페이지 조회 요청이 들어왔습니다.", id);

        log.info("[User Controller - getMyPage]: 사용자 정보 조회를 시작합니다. id: {}", id);
        GetMyPageResponseDto response = memberService.getMyPage(id);
        log.info("[User Controller - getMyPage]: 사용자 정보 조회가 완료되었습니다. id: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "마이페이지 수정 API", description = "마이페이지를 수정합니다.")
    @PatchMapping("/{id}")
    ResponseEntity<CustomResponseCode> updateNickname(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMyPageRequestDto updateMyPageRequestDto) {
        log.info("[User Controller - updateNickname]: {}번 사용자의 마이페이지 수정 요청이 들어왔습니다.", id);
        return memberService.updateMyPage(id, updateMyPageRequestDto);
    }

    @Operation(summary = "사용자 문의 사항 작성 API", description = "사용자의 문의 사항을 작성합니다.")
    @PostMapping("/question/create")
    public ResponseEntity<CustomResponseCode> createQuestion(
            @RequestBody QuestionCreateRequestDto questionCreateRequestDto,
            @RequestHeader("Authorization") @Parameter(description = "문의자 id") String authorization) {
        log.info("[User Controller - createQuestion]: 문의 사항 생성 요청이 들어왔습니다.");
        String content = questionCreateRequestDto.getContent();
        String title = questionCreateRequestDto.getTitle();
        String category = questionCreateRequestDto.getCategory();
        return memberService.createQuestion(content, title, category, authorization);
    }




}

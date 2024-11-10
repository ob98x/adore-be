package com.adminservice.user.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.user.dto.GetMemberListResponseDto;
import com.adminservice.user.dto.GetMemberResponseDto;
import com.adminservice.user.dto.MemberCreateRequestDto;
import com.adminservice.user.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "[관리자] 사용자 관련 API", description = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserController {

    private final MemberService memberService;


    @Operation(summary = "[미사용] 사용자 리스트 조회 API", description = "사용자 리스트를 조회합니다.")
    @GetMapping("/lists/{page}")
    public ResponseEntity<GetMemberListResponseDto> searchUsers(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetMemberListResponseDto response = memberService.searchUsers(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @Operation
    @GetMapping("/list")
    public ResponseEntity<List<GetMemberListResponseDto.MemberListInfo>> allMembers() {
        List<GetMemberListResponseDto.MemberListInfo> response = memberService.allMembers();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 생성 API", description = "사용자를 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<CustomResponseCode> createMember(
            @Valid @RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        return memberService.createMember(memberCreateRequestDto);
    }

    @Operation(summary = "사용자 수정 API", description = "사용자를 수정합니다.")
    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updateMember(
            @Valid @RequestBody MemberCreateRequestDto memberCreateRequestDto, @Parameter(description = "삭제할 수정할 id") @RequestParam Long id) {
        return memberService.updateMember(id, memberCreateRequestDto);
    }

    @Operation(summary = "사용자 조회 API", description = "사용자를 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<GetMemberResponseDto> viewMemberInfo(@Parameter(description = "삭제할 조회할 id") @RequestParam Long id) {
        return ResponseEntity.ok(memberService.getMember(id));
    }

    @Operation(summary = "사용자 삭제 API", description = "사용자를 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteMember(@Parameter(description = "삭제할 사용자 id") @RequestParam Long id) {
        return memberService.deleteMember(id);
    }
}

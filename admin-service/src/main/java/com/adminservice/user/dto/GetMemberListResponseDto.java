package com.adminservice.user.dto;

import com.adminservice.user.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetMemberListResponseDto {

    @Schema(description = "회원 목록", example = "[{\"id\": 1, \"name\": \"홍길동\", \"email\": \"이메일\", \"createdAt\": \"2021-07-01T00:00:00\"}]")
    private List<MemberListInfo> memberList;

    @Schema(description = "총 페이지 수", example = "1")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "false")
    private boolean hasNext;

    @Getter
    @Setter
    public static class MemberListInfo {

        @Schema(description = "회원 ID", example = "1")
        private Long id;

        @Schema(description = "회원 이름", example = "홍길동")
        private String name;

        @Schema(description = "회원 이메일", example = "이메일")
        private String email;

        @Schema(description = "생성일", example = "2021-07-01T00:00:00")
        private LocalDateTime createdAt;

        public static MemberListInfo fromMember(Member member) {
            MemberListInfo info = new MemberListInfo();
            info.setId(member.getId());
            info.setName(member.getName());
            info.setEmail(member.getEmail());
            info.setCreatedAt(member.getCreatedAt());
            return info;
        }
    }
    public static GetMemberListResponseDto createResponse(List<MemberListInfo> memberList, int totalPages, boolean hasNext) {
        GetMemberListResponseDto response = new GetMemberListResponseDto();
        response.setMemberList(memberList);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }

}

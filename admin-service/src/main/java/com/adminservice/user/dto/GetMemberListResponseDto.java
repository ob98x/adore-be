package com.adminservice.user.dto;

import com.adminservice.user.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetMemberListResponseDto {
    private List<MemberListInfo> memberList;
    private int totalPages;
    private boolean hasNext;

    @Getter
    @Setter
    public static class MemberListInfo {
        private Long id;
        private String name;
        private String email;
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

package com.authservice.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTokenInfo {
    private String memberName;
    private String memberRole;
    private Long memberId;

    @Builder
    public GetTokenInfo(String memberName, String memberRole, Long memberId) {
        this.memberName = memberName;
        this.memberRole = memberRole;
        this.memberId = memberId;
    }

    public static GetTokenInfo of(String memberName, String memberRole, Long memberId) {
        return GetTokenInfo.builder()
                .memberName(memberName)
                .memberRole(memberRole)
                .memberId(memberId)
                .build();
    }
}

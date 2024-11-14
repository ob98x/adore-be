package com.authservice.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTokenInfo {

    @Schema(description = "회원 이름", example = "홍길동")
    private String memberName;

    @Schema(description = "회원 권한", example = "ROLE_USER")

    private String memberRole;

    @Schema(description = "회원 ID", example = "1")
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

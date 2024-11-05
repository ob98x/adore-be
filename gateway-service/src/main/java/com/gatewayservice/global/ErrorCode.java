package com.gatewayservice.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FOUND_REFRESH_TOKEN("AUT-ERR-004", HttpStatus.NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다."),
    NOT_FOUND_ACCESS_TOKEN("AUT-ERR-005", HttpStatus.NOT_FOUND, "액세스 토큰을 찾을 수 없습니다."),
    INVALID_TOKEN("AUT-ERR-006", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("AUT-ERR-007", HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN("AUT-ERR-008", HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),
    INVALID_HEADER_OR_COMPACT_JWT("AUT-ERR-009", HttpStatus.UNAUTHORIZED, "헤더 또는 컴팩트 JWT 잘못되었습니다."),
    UNAUTHORIZED("AUT-ERR-010", HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

}
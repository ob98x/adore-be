package com.adminservice.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    //MEMBER
    MEMBER_NOT_FOUND("MEM-ERR-001", HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),

    // AUTH
    PASSWORD_INCORRECT("AUT-ERR-001", HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다"),
    ALREADY_LOGGED_IN("AUT-ERR-002", HttpStatus.BAD_REQUEST, "이미 로그인 되어있습니다."),
    NOT_FOUND_USER("AUT-ERR-003", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_REFRESH_TOKEN("AUT-ERR-004", HttpStatus.NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다."),
    NOT_FOUND_ACCESS_TOKEN("AUT-ERR-005", HttpStatus.NOT_FOUND, "액세스 토큰을 찾을 수 없습니다."),
    INVALID_TOKEN("AUT-ERR-006", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("AUT-ERR-007", HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN("AUT-ERR-008", HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),
    INVALID_HEADER_OR_COMPACT_JWT("AUT-ERR-009", HttpStatus.UNAUTHORIZED, "헤더 또는 컴팩트 JWT 잘못되었습니다."),
    UNAUTHORIZED("AUT-ERR-010", HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    FORBIDDEN("AUT-ERR-011", HttpStatus.FORBIDDEN, "권한이 없습니다."),
    EMAIL_DUPLICATE("AUT-ERR-012", HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    NICKNAME_DUPLICATE("AUT-ERR-013", HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    TERMS_NOT_AGREED("AUT-ERR-014", HttpStatus.BAD_REQUEST, "약관에 동의해주세요."),
    EMAIL_AUTHORIZATION_FAIL("AUT-ERR-015", HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다."),
    EMAIL_AUTHORIZATION_SUCCESS("AUT-ERR-016", HttpStatus.BAD_REQUEST, "이메일 인증에 성공했습니다."),
    EMAIL_SEND_SUCCESS("AUT-ERR-017", HttpStatus.OK, "이메일 전송에 성공했습니다."),
    EMAIL_SEND_FAIL("AUT-ERR-018", HttpStatus.BAD_REQUEST, "이메일 전송에 실패했습니다."),
    EMAIL_COUNT_EXCEED("AUT-ERR-019", HttpStatus.BAD_REQUEST, "이메일 요청 횟수를 초과했습니다."),

    //GLOBAL
    BAD_REQUEST("GLB-ERR-001", HttpStatus.NOT_FOUND, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED("GLB-ERR-002", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR("GLB-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),

    // Question
    NOT_FOUND_MEMBER("MEM-ERR-001", HttpStatus.NOT_FOUND, "멤버가 존재하지 않습니다."),
    NOT_FOUND_QUESTION("QST-ERR-001", HttpStatus.NOT_FOUND, "문의사항이 존재하지 않습니다."),
    INVALID_QUESTION_STATE("QST-ERR-002", HttpStatus.INTERNAL_SERVER_ERROR, "수정할 수 없는 문의사항입니다."),
    INVALID_QUESTION_STATE_COMPLETE("QST-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "답변 완료된 문의사항입니다."),
    INVALID_QUESTION_STATE_DELETE("QST-ERR-004", HttpStatus.INTERNAL_SERVER_ERROR, "삭제된 문의사항입니다."),
    INVALID_QUESTION_SEARCH_CATEGORY("QST-ERR-005", HttpStatus.BAD_REQUEST, "잘못된 카테고리입니다."),
    INVALID_QUESTION_SEARCH_STATE("QST-ERR-006", HttpStatus.BAD_REQUEST, "잘못된 상태입니다."),

    // SURVEY
    SURVEY_NOT_FOUND("SRV-ERR-001", HttpStatus.NOT_FOUND, "설문지를 찾을 수 없습니다."),
    SURVEY_QST_NOT_FOUND("SRV-ERR-002", HttpStatus.NOT_FOUND, "설문에 맞는 질문을 찾을 수 없습니다."),
    SURVEY_ANS_NOT_FOUND("SRV-ERR-003", HttpStatus.NOT_FOUND, "질문에 맞는 답변을 찾을 수 없습니다."),
    SURVEY_USER_ANS_NOT_FOUND("SRV-ERR-004", HttpStatus.NOT_FOUND, "사용자가 작성한 질문을 찾을 수 없습니다."),
    FRIEND_NOT_FOUND("SRV-ERR-005", HttpStatus.NOT_FOUND, "친구를 찾을 수 없습니다."),
    RECOMMEND_NOT_FOUND("SRV-ERR-006", HttpStatus.NOT_FOUND, "추천을 받지 못했습니다."),
    SURVEY_DELETED("SRV-ERR-007", HttpStatus.BAD_REQUEST, "삭제된 설문입니다."),

    // Email
    FAILED_MAIL_CREATE("MAIL-ERR-001", HttpStatus.INTERNAL_SERVER_ERROR, "메일 생성에 실패했습니다."),
    FAILED_MAIL_TEMPLATE("MAIL-ERR-002", HttpStatus.INTERNAL_SERVER_ERROR, "메일 템플릿 불러오기에 실패했습니다."),
    FAILED_MAIL_SEND("MAIL-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다."),

    FILE_NOT_FOUND("HOL-ERR-002", HttpStatus.BAD_REQUEST, "파일이 없습니다."),
    NOT_IMAGE_FILE("GLB-ERR-004", HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다."),
    QUESTION_NOT_FOUND("QST-ERR-001", HttpStatus.NOT_FOUND, "문의사항을 찾을 수 없습니다."),
    QUESTION_DELETED("QST-ERR-002", HttpStatus.BAD_REQUEST, "삭제된 문의사항입니다."),
    REPORT_NOT_FOUND("RPT-ERR-001", HttpStatus.NOT_FOUND, "신고사항을 찾을 수 없습니다."),
    REPORT_DELETED("RPT-ERR-002", HttpStatus.BAD_REQUEST, "삭제된 신고사항입니다."),
    NOTIFICATION_NOT_FOUND("NOT-ERR-001", HttpStatus.BAD_REQUEST, "공지사항을 찾을 수 없습니다."),
    NOTIFICATION_DELETED("NOT-ERR-002", HttpStatus.BAD_REQUEST, "삭제된 공지사항입니다."),
    MEMBER_DELETED("MEM-ERR-02", HttpStatus.BAD_REQUEST, "삭제된 회원입니다."),
    PERFUME_DELETED("PER-ERR-002", HttpStatus.BAD_REQUEST, "삭제된 향수입니다."),
    PERFUME_NOT_FOUND("PER-ERR-001", HttpStatus.NOT_FOUND, "향수를 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED("HOL-ERR-001", HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    INVALID_FILE_TYPE("HOL-ERR-003", HttpStatus.BAD_REQUEST, "잘못된 파일 형식입니다."),
    NOTE_NOT_FOUND("NOT-ERR-003", HttpStatus.NOT_FOUND, "노트를 찾을 수 없습니다."),
    UNAUTHORIZED_MEMBER("MEM-ERR-03", HttpStatus.UNAUTHORIZED, "권한이 없는 회원입니다."),;
    private final String code;
    private final HttpStatus status;
    private final String message;


    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }
}
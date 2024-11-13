package com.adminservice.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomResponseCode {
    NICKNAME_DUPLICATE("닉네임이 중복되었습니다."),
    NICKNAME_NOT_DUPLICATE("사용가능한 닉네임입니다."),
    MEMBER_CREATE_SUCCESS("회원 생성에 성공하였습니다."),
    MEMBER_DELETE_SUCCESS("회원 삭제에 성공하였습니다."),
    MEMBER_UPDATE_SUCCESS("회원 수정에 성공하였습니다."),
    QUESTION_PROCESS_SUCCESS("문의 처리에 성공하였습니다."),
    REPORT_PROCESS_SUCCESS("신고 처리에 성공하였습니다."),
    NOTIFICATION_CREATE_SUCCESS("공지 사항 작성에 성공하였습니다."),
    NOTIFICATION_UPDATE_SUCCESS("공지 사항 수정에 성공하였습니다."),
    NOTIFICATION_DELETE_SUCCESS("공지사항 삭제에 성공하였습니다."),
    PERFUME_DELETE_SUCCESS("향수 삭제에 성공하였습니다."),
    PERFUME_CREATE_SUCCESS("향수 등록에 성공하였습니다."),
    PERFUME_UPDATE_SUCCESS("향수 수정에 성공하였습니다."),
    PERFUME_LIKE_SUCCESS("향수 좋아요에 성공하였습니다."),
    PERFUME_LIKE_CANCEL_SUCCESS("향수 좋아요 취소에 성공하였습니다."),
    PERFUME_LIKE_DUPLICATE("이미 좋아요를 누르셨습니다."),
    PERFUME_LIKE_NOT_FOUND("좋아요를 누르지 않으셨습니다."),
    PERFUME_LIKE_NOT_FOUND_PERFUME("해당 향수를 찾을 수 없습니다."),
    PERFUME_LIKE_NOT_FOUND_MEMBER("해당 회원을 찾을 수 없습니다."),
    PERFUME_LIKE_NOT_FOUND_LIKE("좋아요를 찾을 수 없습니다."),
    PERFUME_LIKE_NOT_FOUND_UNLIKE("좋아요 취소를 찾을 수 없습니다."),
    INVALID_FILE_TYPE("잘못된 파일 형식입니다."),
    NOTE_CREATE_SUCCESS("노트 생성에 성공하였습니다."),
    NOTE_DELETE_SUCCESS("노트 삭제에 성공하였습니다."),
    NOTE_UPDATE_SUCCESS("노트 수정에 성공하였습니다."),;
    private final String message;

}
package com.userservice.global;

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
    MY_PAGE_UPDATE_SUCCESS("마이페이지 수정에 성공하였습니다.");
    private final String message;
}
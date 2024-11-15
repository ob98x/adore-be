package com.adminservice.question.dto;

import com.adminservice.question.entity.Question;
import com.adminservice.question.entity.QuestionState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetQuestionListResponseDto {

    @Schema(description = "문의 목록", example = "[{\"title\": \"문의 제목\", \"nickname\": \"문의자 닉네임\", \"email\": \"문의자 이메일\", \"state\": \"WAITING\", \"createdAt\": \"2021-07-01T00:00:00\"}]")
    private List<QuestionListInfo> questionList;

    @Schema(description = "총 페이지 수", example = "1")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "false")
    private boolean hasNext;

    @Getter
    @Setter
    public static class QuestionListInfo {

        @Schema(description = "문의 제목", example = "문의 제목")
        private String title;

        @Schema(description = "문의자 닉네임", example = "문의자 닉네임")
        private String nickname;

        @Schema(description = "문의자 이메일", example = "문의자 이메일")
        private String email;

        @Schema(description = "문의 상태", example = "WAITING")
        private QuestionState state;

        @Schema(description = "생성일", example = "2021-07-01T00:00:00")
        private LocalDateTime createdAt;

        public static QuestionListInfo fromQuestion(Question question) {
            QuestionListInfo info = new QuestionListInfo();
            info.setTitle(question.getTitle());
            info.setNickname(question.getApplicant().getNickname());
            info.setEmail(question.getApplicant().getEmail());
            info.setState(question.getState());
            info.setCreatedAt(question.getCreatedAt());
            return info;
        }
    }
    public static GetQuestionListResponseDto createResponse(List<QuestionListInfo> questionList, int totalPages, boolean hasNext) {
        GetQuestionListResponseDto response = new GetQuestionListResponseDto();
        response.setQuestionList(questionList);
        response.setTotalPages(totalPages);
        response.setHasNext(hasNext);
        return response;
    }
}

package com.adminservice.question.dto;

import com.adminservice.question.entity.Question;
import com.adminservice.question.entity.QuestionState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetQuestionListResponseDto {

    private List<QuestionListInfo> questionList;
    private int totalPages;
    private boolean hasNext;

    @Getter
    @Setter
    public static class QuestionListInfo {
        private String title;
        private String nickname;
        private String email;
        private QuestionState state;
        private LocalDateTime createdAt;

        // 정적 팩토리 메서드를 추가하여 변환 간소화
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

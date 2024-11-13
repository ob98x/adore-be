package com.adminservice.survey.dto;

import com.adminservice.survey.entity.*;
import com.adminservice.user.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveyCreateRequestDto {
    /**
     * - 작성한 memberId Long
     * - 질문 text, orderState, type, Domain 설정해야함 -> 관리자 페이지에도 이게 필요한데
     * 관리자 페이지에 노트 검색 같은 것들도 필요한데 시발!
     * - 이 질문에 딸린 답변 리스트 (SurveyAns 참고)
     * 만약 업데이트를 고려한다면 surveyId까지 추가하면 그만이다.
     */
    private Long writerMemberId;
    private List<SurveyQuestion> questionList;

    @Getter
    @Setter
    public static class SurveyQuestion {
        private String questionTxt;
        private SurveyQstTypeState questionType;
        private SurveyQstOrderState questionOrder;
        private SurveyQstDomainState questionDomain;
        private List<SurveyAnswer> answerList;

        public static SurveyQuestion of(String questionTxt, SurveyQstTypeState questionType, SurveyQstOrderState questionOrder, SurveyQstDomainState questionDomain, List<SurveyAnswer> answerList) {
            SurveyQuestion qst = new SurveyQuestion();
            qst.setQuestionTxt(questionTxt);
            qst.setQuestionType(questionType);
            qst.setQuestionOrder(questionOrder);
            qst.setQuestionDomain(questionDomain);
            qst.setAnswerList(answerList);
            return qst;
        }

        public static SurveyQst createSurveyQst(Survey survey, SurveyQuestion qst){
            return SurveyQst.builder()
                    .survey(survey)
                    .questionTxt(qst.getQuestionTxt())
                    .questionType(qst.getQuestionType())
                    .questionOrder(qst.getQuestionOrder())
                    .questionDomain(qst.getQuestionDomain())
                    .build();
        }
    }

    @Getter
    @Setter
    public static class SurveyAnswer {
        private String answerTxt;
        private String value;
        private Long nxtQstId;
        /**
         * 일단은 관리자가 작성할 때는 1번 답변은 2번 질문으로 이어줘 이런 식으로 작성할 것이다.
         * 따라서 순서가 들어오게 될 것임.
         * -1을 적는 경우 다음 질문이 없는 것으로 간주한다.
         */

        public static SurveyAnswer of(String answerTxt, String value, Long nxtQstId) {
            SurveyAnswer ans = new SurveyAnswer();
            ans.setAnswerTxt(answerTxt);
            ans.setValue(value);
            ans.setNxtQstId(nxtQstId);
            return ans;
        }

        public static SurveyAns createSurveyAns(SurveyQst thisSurveyQst, Long nxtQstId, SurveyAnswer ans) {

            return SurveyAns.builder()
                    .surveyQst(thisSurveyQst)
                    .answerTxt(ans.getAnswerTxt())
                    .value(ans.getValue())
                    .nxtQstId(nxtQstId)
                    .build();
        }

    }

    public static SurveyCreateRequestDto of(Long writerMemberId, List<SurveyQuestion> questionList) {
        SurveyCreateRequestDto requestDto = new SurveyCreateRequestDto();
        requestDto.setWriterMemberId(writerMemberId);
        requestDto.setQuestionList(questionList);
        return requestDto;
    }

    public static Survey createSurvey(Member member){
        return Survey.builder()
                .member(member)
                .surveyCnt(0)
                .state(SurveyState.ACTIVE)
                .build();
    }
}

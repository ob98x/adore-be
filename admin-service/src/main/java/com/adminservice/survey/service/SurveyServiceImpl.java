package com.adminservice.survey.service;

import com.adminservice.feign.AuthFeignInterface;
import com.adminservice.global.CustomException;
import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.FilterType;
import com.adminservice.global.ResponseCode;
import com.adminservice.survey.dto.GetSurveyListResponseDto;
import com.adminservice.survey.dto.GetSurveyResponseDto;
import com.adminservice.survey.dto.SurveyCreateRequestDto;
import com.adminservice.survey.entity.Survey;
import com.adminservice.survey.entity.SurveyAns;
import com.adminservice.survey.entity.SurveyQst;
import com.adminservice.survey.entity.SurveyState;
import com.adminservice.survey.repository.SurveyAnsRepository;
import com.adminservice.survey.repository.SurveyQstRepository;
import com.adminservice.survey.repository.SurveyRepository;
import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberState;
import com.adminservice.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService{

    private final SurveyRepository surveyRepository;
    private final SurveyAnsRepository surveyAnsRepository;
    private final SurveyQstRepository surveyQstRepository;
    private final MemberRepository memberRepository;

    private final AuthFeignInterface authFeignInterface;

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createSurvey(SurveyCreateRequestDto dto, String authorization) {
        // survey 저장
        Long memberId = getMemberId(authorization);
        Member member = checkConflictMember(memberId);
        Survey survey = surveyRepository.save(SurveyCreateRequestDto.createSurvey(member));
        // surveyQst, surveyAns 저장
        for(SurveyCreateRequestDto.SurveyQuestion surveyQuestion : dto.getQuestionList()){
            SurveyQst qst = surveyQstRepository.save(SurveyCreateRequestDto.SurveyQuestion.createSurveyQst(survey,surveyQuestion));

            List<SurveyAns> surveyAns = new ArrayList<>();
            for(SurveyCreateRequestDto.SurveyAnswer surveyAnswer : surveyQuestion.getAnswerList()) {
                log.info("답변 List 생성 시작 : {}", surveyAnswer.getValue());
                Long nxtQstId = -1L;
                if(surveyAnswer.getNxtQstId() != -1L) {
                    nxtQstId = qst.getId()+surveyAnswer.getNxtQstId()-1L; // 해당 질문을 기준으로 설정해야 함
                }
                log.info("다음 질문 정해짐 : {}", nxtQstId);
                surveyAns.add(SurveyCreateRequestDto.SurveyAnswer.createSurveyAns(qst, nxtQstId, surveyAnswer));
            }
            surveyAnsRepository.saveAll(surveyAns);
        }

        return ResponseEntity.ok(CustomResponseCode.SURVEY_CREATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateSurvey(SurveyCreateRequestDto dto, Long surveyId) {
        // 기존 질문과 답변 삭제
        Survey survey = checkConflictSurvey(surveyId);
        // 질문 가져오기
        List<SurveyQst> qstList = surveyQstRepository.findAllBySurveyId(surveyId);
        // 각 질문의 답변 삭제 후 질문 삭제
        for(SurveyQst q : qstList) {
            surveyAnsRepository.deleteAllBySurveyQstId(q.getId());
            surveyQstRepository.deleteById(q.getId());
        }

        // 새로운 질문과 답변 저장
        for(SurveyCreateRequestDto.SurveyQuestion surveyQuestion : dto.getQuestionList()){
            SurveyQst qst = surveyQstRepository.save(SurveyCreateRequestDto.SurveyQuestion.createSurveyQst(survey,surveyQuestion));
            List<SurveyAns> surveyAns = new ArrayList<>();
            for(SurveyCreateRequestDto.SurveyAnswer surveyAnswer : surveyQuestion.getAnswerList()) {
                Long nxtQstId = -1L;
                if(surveyAnswer.getNxtQstId() != -1L) {
                    nxtQstId = qst.getId()+surveyAnswer.getNxtQstId()-1L;
                }
                surveyAns.add(SurveyCreateRequestDto.SurveyAnswer.createSurveyAns(qst, nxtQstId,surveyAnswer));
            }
            surveyAnsRepository.saveAll(surveyAns);
        }

        return ResponseEntity.ok(CustomResponseCode.SURVEY_UPDATE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteSurvey(Long surveyId) {
        Survey survey = checkConflictSurvey(surveyId);
        survey.setState(SurveyState.INACTIVE);
        surveyRepository.save(survey);
//        // 질문 가져오기
//        List<SurveyQst> qst = surveyQstRepository.findAllBySurveyId(surveyId);
//        // 각 질문의 답변 삭제 후 질문 삭제
//        for(SurveyQst q : qst) {
//            surveyAnsRepository.deleteAllBySurveyQstId(q.getId());
//            surveyQstRepository.deleteById(q.getId());
//        }
        return ResponseEntity.ok(CustomResponseCode.SURVEY_DELETE_SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public GetSurveyResponseDto getSurveyInfo(Long surveyId) {
        Survey survey = checkConflictSurvey(surveyId);
        // 질문 리스트 가져오기
        List<SurveyQst> qstList = surveyQstRepository.findAllBySurveyId(surveyId);
        // 각 질문의 답변들을 가져와서 dto 부품 객체 생성
        List<GetSurveyResponseDto.GetSurveyQuestion> getSurveyQuestionList = new ArrayList<>();
        for(SurveyQst qst : qstList) {
            List<GetSurveyResponseDto.GetSurveyAnswer> getSurveyAnswerList = surveyAnsRepository.findAllBySurveyQstId(qst.getId()).stream()
                    .map(GetSurveyResponseDto.GetSurveyAnswer::fromSurveyAns)
                    .toList();
            getSurveyQuestionList.add(GetSurveyResponseDto.GetSurveyQuestion.fromSurveyQst(qst, getSurveyAnswerList));
        }
        return GetSurveyResponseDto.fromSurvey(survey, getSurveyQuestionList);
    }

    @Override
    @Transactional(readOnly = true)
    public GetSurveyListResponseDto getSurveyList(FilterType filterType, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Specification<Survey> spec = Specification.where(null);
        // 필터 조건 추가
        if (filterType != null ) {
            if (filterType == FilterType.ACTIVE) {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), SurveyState.ACTIVE));
            } else if (filterType == FilterType.INACTIVE) {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), SurveyState.INACTIVE));
            }
        } // 조건이 없으면 전체 조회
        Page<Survey> surveys = surveyRepository.findAll(spec, pageable);
        List<GetSurveyListResponseDto.SurveyListInfo> surveyList = surveys.getContent().stream()
                .map(GetSurveyListResponseDto.SurveyListInfo::fromSurvey)
                .toList();
        return GetSurveyListResponseDto.createResponse(surveyList, surveys.getTotalPages(), surveys.hasNext());
    }

    private Survey checkConflictSurvey(Long id) {
        if (surveyRepository.findByIdAndState(id, SurveyState.ACTIVE).isEmpty()) {
            throw new CustomException(ResponseCode.SURVEY_NOT_FOUND);
        }
        if (surveyRepository.findByIdAndState(id, SurveyState.ACTIVE).get().getState().equals(SurveyState.INACTIVE)) {
            throw new CustomException(ResponseCode.SURVEY_DELETED);
        }
        return surveyRepository.findByIdAndState(id, SurveyState.ACTIVE).get();
    }

    private Member checkConflictMember(Long id) {
        if (memberRepository.findById(id).isEmpty()) {
            throw new CustomException(ResponseCode.MEMBER_NOT_FOUND);
        }
        if (memberRepository.findById(id).get().getState().equals(MemberState.INACTIVE)) {
            throw new CustomException(ResponseCode.MEMBER_DELETED);
        }
        return memberRepository.findById(id).get();
    }

    private Long getMemberId(String authorization) {
        log.info("[Review Service - getMemberId]: 헤더의 Authorization 을 Access Token 으로 변환해 Token의 정보를 받아옵니다 .authorization to token, token: {}, authorization: {}", authorization, authorization.substring(7));
        String accessToken = authorization.substring(7);
        return authFeignInterface.getInfo(accessToken).getMemberId();
    }
}


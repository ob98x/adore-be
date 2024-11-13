package com.userservice.survey.service;

import com.userservice.global.CustomException;
import com.userservice.global.ResponseCode;
import com.userservice.perfume.entity.Perfume;
import com.userservice.perfume.entity.PerfumeState;
import com.userservice.perfume.repository.PerfumeRepository;
import com.userservice.survey.dto.*;
import com.userservice.survey.entity.*;
import com.userservice.survey.repository.*;
import com.userservice.user.entity.Member;
import com.userservice.user.entity.MemberState;
import com.userservice.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService{

    private final SurveyRepository surveyRepository;
    private final SurveyAnsRepository surveyAnsRepository;
    private final SurveyQstRepository surveyQstRepository;
    private final SatisSurveyRepository satisSurveyRepository;
    private final UserAnsRepository userAnsRepository;
    private final RecommResRepository recommResRepository;

    private final FriendRepository friendRepository;
    private final FrRecommResRepository frRecommResRepository;

    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;

    private final RestTemplate restTemplate;

    @Override
    @Transactional(readOnly = true)
    public GetQuestionsDto getFirstQuestions() {
        // 설문 불러오기
        Survey survey = findByState(SurveyState.ACTIVE);
        // 질문 불러오기
        SurveyQst question = findStartAndEndQst(survey.getId(), SurveyQstOrderState.START);
        // 답변 불러오기
        List<GetQuestionsDto.AnsSet> answers = findAnswers(question.getId()).stream()
                .map(GetQuestionsDto.AnsSet::fromAns)
                .toList();
        // dto 생성
        GetQuestionsDto.QstAnsSet qstAnsSet = GetQuestionsDto.QstAnsSet.fromQst(question, answers);
        return GetQuestionsDto.createSingleResponse(qstAnsSet, survey.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public GetQuestionsDto getAdditionalQuestions(Long surveyId, List<Long> nxtQstIds) {
        Survey survey = findSurveyByIdAndState(surveyId, SurveyState.ACTIVE);
        List<SurveyQst> questions = new ArrayList<SurveyQst>();
        for(Long nxtQstId : nxtQstIds){
            questions.add(findMiddleQst(nxtQstId, SurveyQstOrderState.MIDDLE));
        }
        questions.add(findStartAndEndQst(surveyId, SurveyQstOrderState.END));

        List<GetQuestionsDto.QstAnsSet> qstAnsSets = new ArrayList<>();
        for(SurveyQst surveyQst : questions) {
            // 답변 불러오기
            List<GetQuestionsDto.AnsSet> answers = findAnswers(surveyQst.getId()).stream()
                    .map(GetQuestionsDto.AnsSet::fromAns)
                    .toList();
            qstAnsSets.add(GetQuestionsDto.QstAnsSet.fromQst(surveyQst, answers));
        }
        return GetQuestionsDto.createResponses(qstAnsSets, surveyId);
    }

    @Override
    @Transactional
    public GetRecommendPerfumes saveSurveyResultAndGetRecommendPerfume(RequestSurveyResultDto dto) {
        // 설문과 멤버 테이블 불러오기
        Member member = findMember(dto.getMemberId(), MemberState.ACTIVE);
        Survey survey = findSurveyByIdAndState(dto.getSurveyId(), SurveyState.ACTIVE);
        // 데이터 변환
        List<String> notes = new ArrayList<>();
        for(SelectNote note : dto.getNotes()){
            notes.add(note.getNoteName());
        }
        // 사용자 답변 테이블에 저장
        UserAns userAns = userAnsRepository.save(UserAns.of(survey, member, notes, dto.getPrice(), UserAnsState.ACTIVE));
        // 설문 사용횟수 증가 후 저장
        survey.setSurveyCnt(survey.getSurveyCnt()+1);
        surveyRepository.save(survey);

        // fastapi에 향수 추천을 요청
        // 전달할 dto 생성
        RequestRecommedDto reqDto = RequestRecommedDto.toFastApiServer(dto.getNotes(), dto.getPrice(), member.getGender());

        return requestRecommendPerfume(reqDto, userAns);
    }

    @Override
    @Transactional
    public void saveSatisfactionResult(SatisfactionResultDto dto) {
        satisSurveyRepository.save(SatisSurvey.of(findUserAnswer(dto.getUserAnsId(), UserAnsState.ACTIVE), dto.getRating(), dto.getReason()));
    }

    @Override
    @Transactional(readOnly = true)
    public GetRecommendPerfumes getRecommendPerfumeResult(Long memberId) {
        // memberId로 가장 최근의 설문 조회
        UserAns userAns = findLatestUserAnswer(memberId, UserAnsState.ACTIVE);
        // userAnsId로 추천 결과 테이블 조회
        List<RecommRes> recommResList = recommResRepository.findAllByUserAnsId(userAns.getId());
        // dto 생성
        List<GetRecommendPerfume> perfumes = new ArrayList<>();

        for(RecommRes res : recommResList) {
            perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getRecommPerfumeId(), PerfumeState.ACTIVE)));
        }

        return GetRecommendPerfumes.of(perfumes);
    }

    @Override
    @Transactional
    public GetRecommendPerfumes saveFriendSurveyResultAndGetRecommendPerfume(RequestFriendSurveyResultDto dto) {

        Member member = findMember(dto.getMemberId(), MemberState.ACTIVE);
        // 데이터 변환
        List<String> notes = new ArrayList<>();
        for(SelectNote note : dto.getNotes()){
            notes.add(note.getNoteName());
        }
        // Friend 테이블 저장
        Friend friend = friendRepository.save(Friend.of(member, dto.getName(), dto.getGender(), dto.getAge(), notes, dto.getCharacter(), dto.getPrice()));

        // fastapi에 향수 추천을 요청
        // 전달할 dto 생성
        RequestRecommedDto reqDto = RequestRecommedDto.toFastApiServer(dto.getNotes(), dto.getPrice(), friend.getGender());

        return requestFriendRecommendPerfume(reqDto, friend);
    }

    @Override
    @Transactional(readOnly = true)
    public GetRecommendPerfumes getFriendRecommendPerfumeResult(Long memberId) {
        // memberId로 가장 최근의 친구 설문 조회
        Friend friend = findLatestFriendAnswer(memberId);
        // 친구 추천 결과 테이블 조회
        List<FrRecommRes> recommResList = frRecommResRepository.findAllByFriendId(friend.getId());

        // dto 생성
        List<GetRecommendPerfume> perfumes = new ArrayList<>();

        for(FrRecommRes res : recommResList) {
            perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getRecommPerfumeId(), PerfumeState.ACTIVE)));
        }

        return GetRecommendPerfumes.of(perfumes);
    }

    private GetRecommendPerfumes requestRecommendPerfume(RequestRecommedDto dto, UserAns userAns) {
        String apiUrl = "/recomm/perfumes/" + userAns.getId();
        log.info("requestURi : {}",apiUrl);

        // HTTP POST 요청 보내기
        ResponseEntity<GetRecommendResultDto> responseEntity = restTemplate.postForEntity(apiUrl, dto, GetRecommendResultDto.class);
        log.info("ResponseEntity : {}", responseEntity);
        // 응답 값
        GetRecommendResultDto responseBody = responseEntity.getBody();

        List<RecommRes> recommRes = new ArrayList<>();
        List<GetRecommendPerfume> perfumes = new ArrayList<>();
        try {
            log.info("recommend perfume response : {}", responseBody.toString());

            // 추천 값 저장 및 향수 정보 가져오기
            for(GetRecommendResultDto.RecommendResponse res : responseBody.getRecommendations()) {
                recommRes.add(RecommRes.of(userAns, res.getPerfume_id(), res.getPerfume_nm(), res.getCosine_sim()));
                perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getPerfume_id(), PerfumeState.ACTIVE)));
            }
            recommResRepository.saveAll(recommRes);


            return GetRecommendPerfumes.of(perfumes);
        } catch (Exception e){
            log.error("no response", e);
            throw new CustomException(ResponseCode.RECOMMEND_NOT_FOUND);
        }

    }

    private GetRecommendPerfumes requestFriendRecommendPerfume(RequestRecommedDto dto, Friend friend) {

        String apiUrl = "/recomm/perfumes/" + friend.getId();
        log.info("requestURi : {}",apiUrl);

        // HTTP POST 요청 보내기
        ResponseEntity<GetRecommendResultDto> responseEntity = restTemplate.postForEntity(apiUrl, dto, GetRecommendResultDto.class);
        log.info("ResponseEntity : {}", responseEntity);
        // 응답 값
        GetRecommendResultDto responseBody = responseEntity.getBody();

        List<FrRecommRes> recommRes = new ArrayList<>();
        List<GetRecommendPerfume> perfumes = new ArrayList<>();
        try {
            log.info("recommend perfume response : {}", responseBody.toString());

            for(GetRecommendResultDto.RecommendResponse res : responseBody.getRecommendations()) {
                recommRes.add(FrRecommRes.of(friend, res.getPerfume_id(), res.getPerfume_nm(), res.getCosine_sim()));
                perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getPerfume_id(), PerfumeState.ACTIVE)));
            }
            frRecommResRepository.saveAll(recommRes);

            return GetRecommendPerfumes.of(perfumes);

        } catch (Exception e){
            log.error("no response", e);
            throw new CustomException(ResponseCode.RECOMMEND_NOT_FOUND);
        }
    }

    private Survey findByState(SurveyState state) {
        return surveyRepository.findByState(state)
                .orElseThrow(()-> new CustomException(ResponseCode.SURVEY_NOT_FOUND));
    }
    private Survey findSurveyByIdAndState(Long surveyId, SurveyState state) {
        return surveyRepository.findByIdAndState(surveyId, state)
                .orElseThrow(() -> new CustomException(ResponseCode.SURVEY_NOT_FOUND));
    }
    private SurveyQst findStartAndEndQst(Long surveyId, SurveyQstOrderState state) {
        return surveyQstRepository.findBySurveyIdAndQuestionOrder(surveyId, state)
                .orElseThrow(() -> new CustomException(ResponseCode.SURVEY_QST_NOT_FOUND));
    }
    private List<SurveyAns> findAnswers(Long surveyQstId) {
        return surveyAnsRepository.findAllBySurveyQstId(surveyQstId);
    }
    private SurveyQst findMiddleQst(Long surveyQstId, SurveyQstOrderState state) {
        return surveyQstRepository.findByIdAndQuestionOrder(surveyQstId, state)
                .orElseThrow(() -> new CustomException(ResponseCode.SURVEY_QST_NOT_FOUND));
    }
    private Member findMember(Long id, MemberState state) {
        return memberRepository.findByIdAndState(id, state)
                .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
    }
    private Perfume findPerfume(Long id, PerfumeState state) {
        return perfumeRepository.findByIdAndState(id, state)
                .orElseThrow(() -> new CustomException(ResponseCode.PERFUME_NOT_FOUND));
    }
    private UserAns findUserAnswer(Long id, UserAnsState state) {
        return userAnsRepository.findByIdAndState(id, state)
                .orElseThrow(() -> new CustomException(ResponseCode.SURVEY_USER_ANS_NOT_FOUND));
    }
    private UserAns findLatestUserAnswer(Long id, UserAnsState state) {
        return userAnsRepository.findByMemberIdAndStateOrderByCreatedAtDesc(id, state)
                .orElseThrow(() -> new CustomException(ResponseCode.SURVEY_USER_ANS_NOT_FOUND));
        // 이 경우 결과가 없는 사람들은 그냥 결과 버튼을 눌렀을 때 에러가 나와버린다.
    }
    private Friend findLatestFriendAnswer(Long id) {
        return friendRepository.findByMemberIdOrderByCreatedAtDesc(id)
                .orElseThrow(() -> new CustomException(ResponseCode.FRIEND_NOT_FOUND));
    }
}

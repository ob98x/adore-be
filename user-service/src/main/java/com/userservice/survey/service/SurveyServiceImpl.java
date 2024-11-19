package com.userservice.survey.service;

import com.userservice.feign.AuthFeignInterface;
import com.userservice.global.CustomException;
import com.userservice.global.CustomResponseCode;
import com.userservice.global.ResponseCode;
import com.userservice.global.SearchType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final AuthFeignInterface authFeignInterface;


    /**
     * 질문 구조 고정됨 : start 질문 1개, middle 질문 n개, end 질문 1개
     *
     * start 질문은 다중 선택(현재 ERD 구조상 문제로 3개 다중 선택이라는 고정 숫자 사용)
     * 추후 답변 개수를 선택할 수 있도록 수정하여 다중 선택의 n수 고정 해제 가능
     * middle, end는 단일 선택
     *
     * middle이 질문 n개로 구현될 수 있는 이유는 survey_qst와 survey_ans가 링크드 리스트 형식으로 연결되어 있기 때문이다.
     *
     * end는 원하는 가격대를 물어볼 수 있도록 설계함
     *
     */
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
        // 질문 세팅 (링크된 질문들 모두 가져오기)
        List<SurveyQst> questions = new ArrayList<SurveyQst>();
        for(Long nxtQstId : nxtQstIds){
            Long linkedQstId = nxtQstId;
            questions.add(findMiddleQst(nxtQstId, SurveyQstOrderState.MIDDLE));
            while(linkedQstId != -1L){
                SurveyAns ans = findSingleSurveyAns(linkedQstId);
                questions.add(findMiddleQst(ans.getSurveyQst().getId(), SurveyQstOrderState.MIDDLE));
                linkedQstId = ans.getNxtQstId();
            }
        }
        // 마지막 질문 포함하기
        questions.add(findStartAndEndQst(surveyId, SurveyQstOrderState.END));

        // 각 질문에 대한 답변 세팅
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
    public GetRecommendPerfumes saveSurveyResultAndGetRecommendPerfume(RequestSurveyResultDto dto, String authorization) {
        // 설문과 멤버 테이블 불러오기
        Long memberId = getMemberId(authorization);
        Member member = findMember(memberId, MemberState.ACTIVE);
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
    public GetRecommendPerfumes getRecommendPerfumeResult(Long userAnsId) {
        UserAns userAns = findUserAnswer(userAnsId, UserAnsState.ACTIVE);
        // userAns로 이용해서 만족도 조사 여부 조회
        Boolean hasSatisSurvey = satisSurveyRepository.findByUserAnsId(userAns.getId()).isPresent();
        // userAnsId로 추천 결과 테이블 조회
        List<RecommRes> recommResList = recommResRepository.findAllByUserAnsId(userAns.getId());
        // dto 생성
        List<GetRecommendPerfume> perfumes = new ArrayList<>();

        for(RecommRes res : recommResList) {
            perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getRecommPerfumeId(), PerfumeState.ACTIVE)));
        }

        return GetRecommendPerfumes.toMe(perfumes, hasSatisSurvey);
    }

    @Override
    @Transactional
    public GetRecommendPerfumes saveFriendSurveyResultAndGetRecommendPerfume(RequestFriendSurveyResultDto dto, String authorization) {

        Long memberId = getMemberId(authorization);
        Member member = findMember(memberId, MemberState.ACTIVE);
        // 데이터 변환
        List<String> notes = new ArrayList<>();
        for(SelectNote note : dto.getNotes()){
            notes.add(note.getNoteName());
        }
        // Friend 테이블 저장
        Friend friend = friendRepository.save(Friend.of(member, dto.getName(), dto.getGender(), dto.getAge(), notes, dto.getCharacter(), dto.getPrice(), FriendState.ACTIVE));

        // fastapi에 향수 추천을 요청
        // 전달할 dto 생성
        RequestRecommedDto reqDto = RequestRecommedDto.toFastApiServer(dto.getNotes(), dto.getPrice(), friend.getGender());

        return requestFriendRecommendPerfume(reqDto, friend);
    }

    @Override
    @Transactional(readOnly = true)
    public GetRecommendPerfumes getFriendRecommendPerfumeResult(Long friendId) {
        Friend friend = findFriend(friendId, FriendState.ACTIVE);
        // 친구 추천 결과 테이블 조회
        List<FrRecommRes> recommResList = frRecommResRepository.findAllByFriendId(friend.getId());

        // dto 생성
        List<GetRecommendPerfume> perfumes = new ArrayList<>();

        for(FrRecommRes res : recommResList) {
            perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getRecommPerfumeId(), PerfumeState.ACTIVE)));
        }

        return GetRecommendPerfumes.toFriend(perfumes);
    }

    @Override
    @Transactional(readOnly = true)
    public GetSurveyResultListResponseDto getSurveyResultList(SearchType searchType, String keyword, int page, String authorization) {
        Pageable pageable = PageRequest.of(page, 10);
        Long memberId = getMemberId(authorization);

        Specification<UserAns> spec = Specification.where(null);
        if (keyword.isEmpty()) { // 키워드가 없을 경우
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("member_id"), memberId));
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("state"), UserAnsState.ACTIVE));
        } else { // 키워드가 있을 경우
            if (searchType == SearchType.NAME) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("select_notes"), "%" + keyword + "%"));
            } else {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("member_id"), memberId));
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), PerfumeState.ACTIVE));
            }
        }
        Page<UserAns> listPage = userAnsRepository.findAll(spec, pageable);

        List<GetSurveyResultListResponseDto.SurveyListInfo> surveyList = new ArrayList<>();
        for(UserAns userAns : listPage.getContent()) {
            // 향수 이름들 가져오기
            List<RecommRes> recommRes = recommResRepository.findAllByUserAnsId(userAns.getId());
            List<RecommendPerfumeNameList> list = recommRes.stream()
                    .map(it -> RecommendPerfumeNameList.of(it.getRecommPerfumeNm()))
                    .toList();
            surveyList.add(GetSurveyResultListResponseDto.SurveyListInfo.fromUserAns(userAns, list));
        }
        return GetSurveyResultListResponseDto.createResponse(surveyList, listPage.getTotalPages(), listPage.hasNext());
    }

    @Override
    @Transactional(readOnly = true)
    public GetFriendResultListResponseDto getFriendResultList(SearchType searchType, String keyword, int page, String authorization) {
        Pageable pageable = PageRequest.of(page, 10);
        Long memberId = getMemberId(authorization);

        Specification<Friend> spec = Specification.where(null);
        if (keyword.isEmpty()) { // 키워드가 없을 경우
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("member_id"), memberId));
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("state"), FriendState.ACTIVE)); // Friend
        } else { // 키워드가 있을 경우
            if (searchType == SearchType.NAME) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("name"), "%" + keyword + "%"));
            } else if (searchType == SearchType.GENDER) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("name"), "%" + keyword + "%"));
            }
            else {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("member_id"), memberId));
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), FriendState.ACTIVE));
            }
        }

        Page<Friend> listPage = friendRepository.findAll(spec, pageable);

        List<GetFriendResultListResponseDto.FriendListInfo> friendList = listPage.getContent().stream()
                .map(GetFriendResultListResponseDto.FriendListInfo::fromFriend)
                .toList();
        return GetFriendResultListResponseDto.createResponse(friendList, listPage.getTotalPages(), listPage.hasNext());
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteUserAns(Long userAnsId, String authorization) {
        UserAns userAns = findUserAnswer(userAnsId,UserAnsState.ACTIVE);
        Long requestMemberId = getMemberId(authorization);
        checkAuthorizeMember(userAns.getMember().getId(), requestMemberId);

        userAns.setState(UserAnsState.INACTIVE);
        // 추천 결과 삭제 필요
        recommResRepository.deleteAllByUserAnsId(userAns.getId());
        // 이후 저장
        userAnsRepository.save(userAns);
        return ResponseEntity.ok(CustomResponseCode.USER_ANS_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteFriend(Long friendId, String authorization) {
        Friend friend = findFriend(friendId, FriendState.ACTIVE);
        Long requestMemberId = getMemberId(authorization);
        checkAuthorizeMember(friend.getMember().getId(), requestMemberId);

        friend.setState(FriendState.INACTIVE);
        // 추천 결과 삭제 필요
        frRecommResRepository.deleteAllByFriendId(friend.getId());
        // 이후 저장
        friendRepository.save(friend);
        return ResponseEntity.ok(CustomResponseCode.FRIEND_DELETE_SUCCESS);
    }

    private GetRecommendResultDto getRecommendResult(RequestRecommedDto dto, Long id) {
        String apiUrl = "/recomm/perfumes/" + id;
        log.info("requestURi : {}",apiUrl);

        // HTTP POST 요청 보내기
        ResponseEntity<GetRecommendResultDto> responseEntity = restTemplate.postForEntity(apiUrl, dto, GetRecommendResultDto.class);
        log.info("ResponseEntity : {}", responseEntity);
        // 응답 값
        return responseEntity.getBody();
    }
    private GetRecommendPerfumes requestRecommendPerfume(RequestRecommedDto dto, UserAns userAns) {

        GetRecommendResultDto responseBody = getRecommendResult(dto, userAns.getId());

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


            return GetRecommendPerfumes.toMe(perfumes, false);
        } catch (Exception e){
            log.error("no response", e);
            throw new CustomException(ResponseCode.RECOMMEND_NOT_FOUND);
        }

    }
    private GetRecommendPerfumes requestFriendRecommendPerfume(RequestRecommedDto dto, Friend friend) {

        GetRecommendResultDto responseBody = getRecommendResult(dto, friend.getId());

        List<FrRecommRes> recommRes = new ArrayList<>();
        List<GetRecommendPerfume> perfumes = new ArrayList<>();
        try {
            log.info("recommend perfume response : {}", responseBody.toString());

            for(GetRecommendResultDto.RecommendResponse res : responseBody.getRecommendations()) {
                recommRes.add(FrRecommRes.of(friend, res.getPerfume_id(), res.getPerfume_nm(), res.getCosine_sim()));
                perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getPerfume_id(), PerfumeState.ACTIVE)));
            }
            frRecommResRepository.saveAll(recommRes);

            return GetRecommendPerfumes.toFriend(perfumes);

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

    private SurveyAns findSingleSurveyAns(Long surveyQstId) {
        return surveyAnsRepository.findBySurveyQstId(surveyQstId)
                .orElseThrow(() -> new CustomException(ResponseCode.SURVEY_ANS_NOT_FOUND));
    }
    private Friend findFriend(Long friendId, FriendState state) {
        return friendRepository.findByIdAndState(friendId, state)
                .orElseThrow(() -> new CustomException(ResponseCode.FRIEND_NOT_FOUND));
    }

    private Long getMemberId(String authorization) {
        log.info("[Review Service - getMemberId]: 헤더의 Authorization 을 Access Token 으로 변환해 Token의 정보를 받아옵니다 .authorization to token, token: {}, authorization: {}", authorization, authorization.substring(7));
        String accessToken = authorization.substring(7);
        return authFeignInterface.getInfo(accessToken).getMemberId();
    }

    private void checkAuthorizeMember(Long writerId, Long requestMemberId) {
        log.info("[Review Service - checkAuthorizeMember]: 작성자와 요청자가 일치하는 지 확인합니다. writerId: {}, requestMemberId: {}", writerId, requestMemberId);
        if (!writerId.equals(requestMemberId)) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_MEMBER);
        }
    }
}

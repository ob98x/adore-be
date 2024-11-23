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
        log.info("[Survey Service - getFirstQuestions]: 첫번째 질문을 조회합니다.");
        // 설문 불러오기
        Survey survey = findTopByState(SurveyState.ACTIVE); // 가장 상위의 active 설문 가져오기
        log.info("[Survey Service - getFirstQuestions]: 가장 최근의 설문을 조회합니다. surveyId : {}", survey.getId());
        // 질문 불러오기
        SurveyQst question = findStartAndEndQst(survey.getId(), SurveyQstOrderState.START);
        log.info("[Survey Service - getFirstQuestions]: {}번 설문의 질문을 조회합니다. qstId : {}", survey.getId(), question.getId());
        // 답변 불러오기
        List<GetQuestionsDto.AnsSet> answers = findAnswers(question.getId()).stream()
                .map(GetQuestionsDto.AnsSet::fromAns)
                .toList();
        log.info("[Survey Service - getFirstQuestions]: {}번 질문의 답변 셋을 조회합니다.", survey.getId());
        // dto 생성
        GetQuestionsDto.QstAnsSet qstAnsSet = GetQuestionsDto.QstAnsSet.fromQst(question, answers);
        return GetQuestionsDto.createSingleResponse(qstAnsSet, survey.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public GetQuestionsDto getAdditionalQuestions(Long surveyId, List<Long> nxtQstIds) {
//        Survey survey = findSurveyByIdAndState(surveyId, SurveyState.ACTIVE);
        // 질문 세팅 (링크된 질문들 모두 가져오기)
        List<SurveyQst> questions = new ArrayList<SurveyQst>();
        for(Long nxtQstId : nxtQstIds){
            Long linkedQstId = nxtQstId;
            log.info("[Survey Service - getAdditionalQuestions]: 첫번째 질문에서 선택한 답변과 연계된 질문을 조회합니다. surveyId : {}, nxtQstId : {}",surveyId, nxtQstId);
            while(linkedQstId != -1L){
                log.info("[Survey Service - getAdditionalQuestions]: while start, 다음 질문 아이디가 -1이 나올때까지 반복하며 질문을 조회하고 응답에 추가합니다.");

                log.info("linkedQstId : {}", linkedQstId);
                SurveyQst surveyQst = findMiddleQst(linkedQstId, SurveyQstOrderState.MIDDLE);
                log.info("find middle qst : {}", surveyQst.getId());
                questions.add(surveyQst);
                log.info("add middle qst : {}", surveyQst.getId());

                log.info("answer find start");
                List<SurveyAns> answers = surveyAnsRepository.findAllBySurveyQstId(linkedQstId);
                log.info("answer find end, {}", answers.stream().toList());

                SurveyAns ans = answers.get(0);
                log.info("answer : {}", ans.getId());
                log.info("답변에 연결된 nxtQstId : {}", ans.getNxtQstId());
                linkedQstId = ans.getNxtQstId();
            }
        }
        // 마지막 질문 포함하기
        questions.add(findStartAndEndQst(surveyId, SurveyQstOrderState.END));
        log.info("[Survey Service - getAdditionalQuestions]: 연계된 질문을 모두 조회했습니다.");

        // 각 질문에 대한 답변 세팅
        List<GetQuestionsDto.QstAnsSet> qstAnsSets = new ArrayList<GetQuestionsDto.QstAnsSet>();
        for(SurveyQst surveyQst : questions) {
            log.info("[Survey Service - getAdditionalQuestions]: 조회한 각 질문들의 답변을 조회합니다. qstId : {}", surveyQst.getId());
            // 답변 불러오기
            List<GetQuestionsDto.AnsSet> answers = findAnswers(surveyQst.getId()).stream()
                    .map(GetQuestionsDto.AnsSet::fromAns)
                    .toList();
            log.info("[Survey Service - getAdditionalQuestions]: {}번 질문의 답변을 조회했습니다. answers : {}", surveyQst.getId(), answers.stream().toList());
            GetQuestionsDto.QstAnsSet ansSet = GetQuestionsDto.QstAnsSet.fromQst(surveyQst, answers);
            qstAnsSets.add(ansSet);
        }
        log.info("[Survey Service - getAdditionalQuestions]: 연계된 질문과 답변을 모두 조회했습니다.");
        return GetQuestionsDto.createResponses(qstAnsSets, surveyId);
    }

    @Override
    @Transactional
    public GetRecommendPerfumes saveSurveyResultAndGetRecommendPerfume(RequestSurveyResultDto dto, String authorization) {
        log.info("[Survey Service - saveSurveyResultAndGetRecommendPerfume]: 설문 결과를 저장하고 추천을 요청합니다. surveyId : {}", dto.getSurveyId());
        // 설문과 멤버 테이블 불러오기
        Long memberId = getMemberId(authorization);
        Member member = findMember(memberId, MemberState.ACTIVE);
        Survey survey = findSurveyByIdAndState(dto.getSurveyId(), SurveyState.ACTIVE);
        // 데이터 변환
        List<String> notes = new ArrayList<String>();
        for(SelectNote note : dto.getNotes()){
            log.info("[Survey Service - saveSurveyResultAndGetRecommendPerfume]: 사용자가 선택한 노트입니다. note : {}", note);
            notes.add(note.getNoteName());
        }
        // 사용자 답변 테이블에 저장
        UserAns userAns = userAnsRepository.save(UserAns.of(survey, member, notes, dto.getPrice(), UserAnsState.ACTIVE));
        // 설문 사용횟수 증가 후 저장
        survey.setSurveyCnt(survey.getSurveyCnt()+1);
        log.info("[Survey Service - saveSurveyResultAndGetRecommendPerfume]: 작성한 설문을 저장하고 설문 사용횟수를 증가시킵니다. surveyId : {}, surveyCnt: {}", survey.getId(), survey.getSurveyCnt());
        surveyRepository.save(survey);

        // fastapi에 향수 추천을 요청
        // 전달할 dto 생성
        RequestRecommedDto reqDto = RequestRecommedDto.toFastApiServer(dto.getNotes(), dto.getPrice(), member.getGender());

        return requestRecommendPerfume(reqDto, userAns);
    }

    @Override
    @Transactional
    public void saveSatisfactionResult(SatisfactionResultDto dto) {
        log.info("[Survey Service - saveSatisfactionResult]: 설문 결과에 대한 만족도 조사 결과를 저장합니다. userAnsId : {}",dto.getUserAnsId());
        satisSurveyRepository.save(SatisSurvey.of(findUserAnswer(dto.getUserAnsId(), UserAnsState.ACTIVE), dto.getRating(), dto.getReason()));
    }

    @Override
    @Transactional(readOnly = true)
    public GetRecommendPerfumes getRecommendPerfumeResult(Long userAnsId) {
        log.info("[Survey Service - getRecommendPerfumeResult]: 추천 결과를 세부조회합니다. userAnsId : {}", userAnsId);
        UserAns userAns = findUserAnswer(userAnsId, UserAnsState.ACTIVE);
        // userAns로 이용해서 만족도 조사 여부 조회
        Boolean hasSatisSurvey = satisSurveyRepository.findByUserAnsId(userAns.getId()).isPresent();
        log.info("[Survey Service - getRecommendPerfumeResult]: 만족도 조사 여부를 확인합니다. : {}", hasSatisSurvey);
        // userAnsId로 추천 결과 테이블 조회
        List<RecommRes> recommResList = recommResRepository.findAllByUserAnsId(userAns.getId());
        // dto 생성
        List<GetRecommendPerfume> perfumes = new ArrayList<GetRecommendPerfume>();

        for(RecommRes res : recommResList) {
            log.info("[Survey Service - getRecommendPerfumeResult]: 추천한 향수들을 조회합니다. : {}", res.getRecommPerfumeNm());
            perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getRecommPerfumeId(), PerfumeState.ACTIVE)));
        }

        return GetRecommendPerfumes.toMe(userAnsId, perfumes, hasSatisSurvey);
    }

    @Override
    @Transactional
    public GetRecommendPerfumes saveFriendSurveyResultAndGetRecommendPerfume(RequestFriendSurveyResultDto dto, String authorization) {
        log.info("[Survey Service - saveFriendSurveyResultAndGetRecommendPerfume]: 친구 설문 결과를 저장하고 추천을 요청합니다. 친구이름 : {}", dto.getName());

        Long memberId = getMemberId(authorization);
        Member member = findMember(memberId, MemberState.ACTIVE);
        // 데이터 변환
        List<String> notes = new ArrayList<String>();
        for(SelectNote note : dto.getNotes()){
            log.info("[Survey Service - saveFriendSurveyResultAndGetRecommendPerfume]: 친구 선호 노트입니다. note : {}", note);
            notes.add(note.getNoteName());
        }
        // Friend 테이블 저장
        Friend friend = friendRepository.save(Friend.of(member, dto.getName(), dto.getGender(), dto.getAge(), notes, dto.getCharacter(), dto.getPrice(), FriendState.ACTIVE));
        log.info("[Survey Service - saveFriendSurveyResultAndGetRecommendPerfume]: 친구 정보와 설문 내용을 저장합니다. friendId : {}", friend.getId());
        // fastapi에 향수 추천을 요청
        // 전달할 dto 생성
        RequestRecommedDto reqDto = RequestRecommedDto.toFastApiServer(dto.getNotes(), dto.getPrice(), friend.getGender());

        return requestFriendRecommendPerfume(reqDto, friend);
    }

    @Override
    @Transactional(readOnly = true)
    public GetRecommendPerfumes getFriendRecommendPerfumeResult(Long friendId) {
        log.info("[Survey Service - getFriendRecommendPerfumeResult]: 친구에게 추천한 결과를 세부조회합니다. friendId : {}", friendId);
        Friend friend = findFriend(friendId, FriendState.ACTIVE);
        // 친구 추천 결과 테이블 조회
        List<FrRecommRes> recommResList = frRecommResRepository.findAllByFriendId(friend.getId());

        // dto 생성
        List<GetRecommendPerfume> perfumes = new ArrayList<>();

        for(FrRecommRes res : recommResList) {
            log.info("[Survey Service - getFriendRecommendPerfumeResult]: 추천한 향수들을 조회합니다. : {}", res.getRecommPerfumeNm());
            perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getRecommPerfumeId(), PerfumeState.ACTIVE)));
        }

        return GetRecommendPerfumes.toFriend(friendId, perfumes);
    }

    @Override
    @Transactional(readOnly = true)
    public GetSurveyResultListResponseDto getSurveyResultList(SearchType searchType, String keyword, int page, String authorization) {
        Pageable pageable = PageRequest.of(page, 10);
        Long memberId = getMemberId(authorization);
        log.info("[Survey Service - getSurveyResultList]: 사용자 ID : {} 의 추천 결과 목록을 조회합니다. searchType : {}, keyword : {}, page : {}", memberId, searchType, keyword, page);

        Specification<UserAns> spec = Specification.where(null);
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("member").get("id"), memberId));
        if (keyword.isEmpty()) { // 키워드가 없을 경우
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("state"), UserAnsState.ACTIVE));
        } else { // 키워드가 있을 경우
            if (searchType == SearchType.NAME) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("select_notes"), "%" + keyword + "%"));
            } else {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), PerfumeState.ACTIVE));
            }
        }
        log.info("[Survey Service - getSurveyResultList]: 추천 결과 목록을 DB에서 조회합니다.");
        Page<UserAns> listPage = userAnsRepository.findAll(spec, pageable);

        List<GetSurveyResultListResponseDto.SurveyListInfo> surveyList = new ArrayList<GetSurveyResultListResponseDto.SurveyListInfo>();
        for(UserAns userAns : listPage.getContent()) {
            log.info("[Survey Service - getSurveyResultList]: 각 추천 목록에서 보여줄 항목들을 불러옵니다. userAnsId : {}", userAns.getId());
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
        log.info("[Survey Service - getFriendResultList]:  사용자 ID : {}의 친구 추천 결과 목록을 조회합니다.", memberId);

        Specification<Friend> spec = Specification.where(null);
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("member").get("id"), memberId));
        if (keyword.isEmpty()) { // 키워드가 없을 경우
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("state"), FriendState.ACTIVE));
        } else { // 키워드가 있을 경우
            if (searchType == SearchType.NAME) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("name"), "%" + keyword + "%"));
            } else if (searchType == SearchType.GENDER) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("gender"), "%" + keyword + "%"));
            }
            else {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), FriendState.ACTIVE));
            }
        }
        log.info("[Survey Service - getFriendResultList]: 친구 추천 결과 목록을 DB에서 조회합니다.");
        Page<Friend> listPage = friendRepository.findAll(spec, pageable);

        log.info("[Survey Service - getFriendResultList]: 친구 추천 결과 목록 DTO를 생성합니다.");
        List<GetFriendResultListResponseDto.FriendListInfo> friendList = listPage.getContent().stream()
                .map(GetFriendResultListResponseDto.FriendListInfo::fromFriend)
                .toList();
        return GetFriendResultListResponseDto.createResponse(friendList, listPage.getTotalPages(), listPage.hasNext());
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteUserAns(Long userAnsId, String authorization) {
        log.info("[Survey Service - deleteUserAns]: {}번 설문 결과를 삭제합니다.", userAnsId);
        UserAns userAns = findUserAnswer(userAnsId,UserAnsState.ACTIVE);
        Long requestMemberId = getMemberId(authorization);
        checkAuthorizeMember(userAns.getMember().getId(), requestMemberId);

        userAns.setState(UserAnsState.INACTIVE);
        // 추천 결과 삭제 필요
        log.info("[Survey Service - deleteUserAns]: 설문 결과에 포함된 추천 결과를 삭제합니다.(hard delete)");
        recommResRepository.deleteAllByUserAnsId(userAns.getId());
        // 이후 저장
        log.info("[Survey Service - deleteUserAns]: 설문 결과를 삭제합니다.(soft delete)");
        userAnsRepository.save(userAns);
        return ResponseEntity.ok(CustomResponseCode.USER_ANS_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteFriend(Long friendId, String authorization) {
        log.info("[Survey Service - deleteFriend]: {}번 친구 설문 결과를 삭제합니다.", friendId);
        Friend friend = findFriend(friendId, FriendState.ACTIVE);
        Long requestMemberId = getMemberId(authorization);
        checkAuthorizeMember(friend.getMember().getId(), requestMemberId);

        friend.setState(FriendState.INACTIVE);
        // 추천 결과 삭제 필요
        log.info("[Survey Service - deleteFriend]: 친구 설문 결과에 포함된 추천 결과를 삭제합니다.(hard delete)");
        frRecommResRepository.deleteAllByFriendId(friend.getId());
        // 이후 저장
        log.info("[Survey Service - deleteFriend]: 친구 설문 결과를 삭제합니다.(soft delete)");
        friendRepository.save(friend);
        return ResponseEntity.ok(CustomResponseCode.FRIEND_DELETE_SUCCESS);
    }

    private GetRecommendResultDto getRecommendResult(RequestRecommedDto dto, Long id) {
        log.info("[Survey Service - getRecommendResult]: 요청한 설문 결과의 아이디 : {}", id);
        String apiUrl = "/recomm/perfumes/" + id;

        // HTTP POST 요청 보내기
        ResponseEntity<GetRecommendResultDto> responseEntity = restTemplate.postForEntity(apiUrl, dto, GetRecommendResultDto.class);
        log.info("[Survey Service - getRecommendResult]: 추천 서버로부터 응답을 받았습니다.");

        // 응답 값
        return responseEntity.getBody();
    }
    private GetRecommendPerfumes requestRecommendPerfume(RequestRecommedDto dto, UserAns userAns) {
        Long userAnsId = userAns.getId();
        log.info("[Survey Service - requestRecommendPerfume]: userAnsId {}번 설문 결과로 추천 서버에 향수 추천을 요청합니다.", userAnsId);

        GetRecommendResultDto responseBody = getRecommendResult(dto, userAnsId);

        List<RecommRes> recommRes = new ArrayList<RecommRes>();
        List<GetRecommendPerfume> perfumes = new ArrayList<GetRecommendPerfume>();
        try {
            log.info("recommend perfume response : {}", responseBody.toString());

            // 추천 값 저장 및 향수 정보 가져오기
            for(GetRecommendResultDto.RecommendResponse res : responseBody.getRecommendations()) {
                recommRes.add(RecommRes.of(userAns, res.getPerfume_id(), res.getPerfume_nm(), res.getCosine_sim()));
                perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getPerfume_id(), PerfumeState.ACTIVE)));
            }
            recommResRepository.saveAll(recommRes);


            return GetRecommendPerfumes.toMe(userAnsId, perfumes, false);
        } catch (Exception e){
            log.error("no recommend response", e);
            throw new CustomException(ResponseCode.RECOMMEND_NOT_FOUND);
        }

    }
    private GetRecommendPerfumes requestFriendRecommendPerfume(RequestRecommedDto dto, Friend friend) {
        Long friendId = friend.getId();
        log.info("[Survey Service - requestFriendRecommendPerfume]: friendId {}번 설문 결과로 추천 서버에 향수 추천을 요청합니다.", friendId);
        GetRecommendResultDto responseBody = getRecommendResult(dto, friendId);

        List<FrRecommRes> recommRes = new ArrayList<FrRecommRes>();
        List<GetRecommendPerfume> perfumes = new ArrayList<GetRecommendPerfume>();
        try {
            log.info("recommend perfume response : {}", responseBody.toString());

            for(GetRecommendResultDto.RecommendResponse res : responseBody.getRecommendations()) {
                recommRes.add(FrRecommRes.of(friend, res.getPerfume_id(), res.getPerfume_nm(), res.getCosine_sim()));
                perfumes.add(GetRecommendPerfume.fromPerfume(findPerfume(res.getPerfume_id(), PerfumeState.ACTIVE)));
            }
            frRecommResRepository.saveAll(recommRes);

            return GetRecommendPerfumes.toFriend(friendId, perfumes);

        } catch (Exception e){
            log.error("no recommend response", e);
            throw new CustomException(ResponseCode.RECOMMEND_NOT_FOUND);
        }
    }

    private Survey findTopByState(SurveyState state) {
        return surveyRepository.findTopByStateOrderByCreatedAt(state)
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

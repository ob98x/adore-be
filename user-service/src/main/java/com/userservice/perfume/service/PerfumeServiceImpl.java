package com.userservice.perfume.service;

import com.userservice.feign.AuthFeignInterface;
import com.userservice.global.CustomException;
import com.userservice.global.CustomResponseCode;
import com.userservice.global.ResponseCode;
import com.userservice.global.SearchType;
import com.userservice.perfume.dto.*;
import com.userservice.perfume.entity.Note;
import com.userservice.perfume.entity.Perfume;
import com.userservice.perfume.entity.PerfumeState;

import com.userservice.perfume.entity.Rating;
import com.userservice.perfume.repository.NoteRepository;
import com.userservice.perfume.repository.PerfumeRepository;

import com.userservice.perfume.repository.RatingRepository;
import com.userservice.user.entity.Member;
import com.userservice.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerfumeServiceImpl implements PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final NoteRepository noteRepository;
    private final AuthFeignInterface authFeignInterface;
    private final MemberService memberService;
    private final RatingRepository ratingRepository;


    @Override
    @Transactional(readOnly = true)
    public GetPerfumeResponseDto getPerfume(Long id) {
        log.info("[Perfume Service - getPerfume]: 향수 정보 조회 요청이 들어왔습니다. id: {}", id);
        return GetPerfumeResponseDto.getPerfume(checkConflictPerfume(id));
    }

    @Override
    @Transactional(readOnly = true)
    public GetNoteResponseDto getNote(Long id) {
        log.info("[Perfume Service - getNote]: 향수 노트 조회 요청이 들어왔습니다. id: {}", id);
        return GetNoteResponseDto.getNote(checkConflictNote(id));
    }

    @Override
    @Transactional(readOnly = true)
    public GetPerfumeListResponseDto searchPerfumes(SearchType searchType, String keyword, int page) {
        log.info("[Perfume Service - searchPerfume]: 향수 리스트 조회 요청이 들어왔습니다. type: {}, keyword: {}", searchType, keyword);
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        log.info("[Perfume Service - searchPerfume]: 검색 조건을 설정합니다.");
        Specification<Perfume> spec = Specification.where(null);
        if (keyword.isEmpty()) { // 키워드가 없을 경우
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("state"), PerfumeState.ACTIVE));
        } else { // 키워드가 있을 경우
            if (searchType == SearchType.NAME) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("name"), "%" + keyword + "%"));
            } else if (searchType == SearchType.BRAND) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("brand"), "%" + keyword + "%"));
            } else {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), PerfumeState.ACTIVE));
            }
        }

        log.info("[Perfume Service - searchPerfume]: 향수 리스트를 DB 에서 가져옵니다.");
        Page<Perfume> resultPage = perfumeRepository.findAll(spec, pageable);

        log.info("[Perfume Service - searchPerfume]: 향수 리스트를 DTO 로 변환합니다.");
        List<GetPerfumeListResponseDto.PerfumeListInfo> perfumeList = resultPage.getContent().stream()
                .map(GetPerfumeListResponseDto.PerfumeListInfo::fromPerfume)
                .toList();

        return GetPerfumeListResponseDto.createResponse(perfumeList, resultPage.getTotalPages(), resultPage.hasNext());
    }


    @Override
    @Transactional(readOnly = true)
    public List<GetParentNoteResponseDto> getParentNotes() {
        log.info("[Perfume Service - getParentNotes]: 부모 노트 리스트 조회 요청이 들어왔습니다.");
        List<Note> parentNote = noteRepository.findNotesByParentNoteId(-1L).get();
        return GetParentNoteResponseDto.fromNoteList(parentNote);
    }

    @Override
    @Transactional
    public CustomResponseCode ratingPerfume(Long perfumeId, String authorization, int rating) {
        log.info("[Perfume Service - ratingPerfume]: 향수 평가 요청이 들어왔습니다. perfumeId: {}, token: {}, rating: {}", perfumeId, authorization, rating);

        Perfume perfume = checkConflictPerfume(perfumeId);

        log.info("[Perfume Service - ratingPerfume]: 멤버를 찾습니다.");
        Long memberId = getMemberId(authorization);
        Member member = memberService.checkConflictMember(memberId);

        if (ratingRepository.findByMemberIdAndPerfumeId(memberId, perfumeId).isPresent()) {
            log.error("[Perfume Service - ratingPerfume]: 이미 평가한 향수입니다.");
            throw new CustomException(ResponseCode.ALREADY_RATED);
        }

        log.info("[Perfume Service - ratingPerfume]: 평가를 저장합니다.");
        perfume.setRateCnt(perfume.getRateCnt() + 1);
        perfume.setRateValue(perfume.getRateValue() + rating);

        ratingRepository.save(Rating.builder()
                .member(member)
                .perfume(perfume)
                .rating(rating)
                .build());

        return CustomResponseCode.RATING_SUCCESS;
    }

    @Override
    @Transactional(readOnly = true)
    public GetNoteListResponseDto searchNotes(Long parentId, int page) {

        log.info("[Perfume Service - searchNotes]: 향수 노트 리스트 조회 요청이 들어왔습니다. parentId: {}", parentId);
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        log.info("[Perfume Service - searchNotes]: 검색 조건을 설정합니다.");
        Specification<Note> spec = Specification.where(null);
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("parentNoteId"), parentId));

        log.info("[Perfume Service - searchNotes]: 향수 노트 리스트를 DB 에서 가져옵니다.");
        Page<Note> resultPage = noteRepository.findAll(spec, pageable);

        log.info("[Perfume Service - searchNotes]: 향수 노트 리스트를 DTO 로 변환합니다.");
        List<GetNoteListResponseDto.NoteListInfo> noteList = resultPage.getContent().stream()
                .map(GetNoteListResponseDto.NoteListInfo::fromNote)
                .toList();
        log.info("[Perfume Service - searchNotes]: 향수 노트 리스트를 반환합니다.");
        log.info("{}개의 노트를 반환합니다.", noteList.size());
        log.info("페이지 정보: totalPages: {}, hasNext: {}", resultPage.getTotalPages(), resultPage.hasNext());
        log.info("note 0 name: {}", noteList.get(0).getNoteNm());

        return GetNoteListResponseDto.createResponse(noteList, resultPage.getTotalPages(), resultPage.hasNext());
    }

    @Override
    public Perfume checkConflictPerfume(Long id) {
        log.info("[Perfume Service - checkConflictPerfume]: id: {}의 향수를 찾습니다.", id);
        if (perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).isEmpty()) {
            log.error("[Perfume Service - checkConflictPerfume]: 향수를 찾을 수 없습니다.");
            throw new CustomException(ResponseCode.PERFUME_NOT_FOUND);
        }
        if (perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).get().getState().equals(PerfumeState.INACTIVE)) {
            log.error("[Perfume Service - checkConflictPerfume]: 향수가 삭제되었습니다.");
            throw new CustomException(ResponseCode.PERFUME_DELETED);
        }
        return perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).get();
    }

    @Override
    public Note checkConflictNote(Long id) {
        log.info("[Perfume Service - checkConflictNote]: id: {}의 노트를 찾습니다.", id);
        if (noteRepository.findNoteById(id).isEmpty()) {
            log.error("[Perfume Service - checkConflictNote]: 노트를 찾을 수 없습니다.");
            throw new CustomException(ResponseCode.NOTE_NOT_FOUND);
        } else {
            return noteRepository.findNoteById(id).get();
        }
    }

    public Long getMemberId(String authorization) {
        log.info("[Review Service - getMemberId]: 헤더의 Authorization 을 Access Token 으로 변환해 Token의 정보를 받아옵니다 .authorization to token, token: {}, authorization: {}", authorization, authorization.substring(7));
        String accessToken = authorization.substring(7);
        return authFeignInterface.getInfo(accessToken).getMemberId();
    }

    public void checkAuthorizeMember(Long writerId, Long requestMemberId) {
        log.info("[Review Service - checkAuthorizeMember]: 작성자와 요청자가 일치하는 지 확인합니다. writerId: {}, requestMemberId: {}", writerId, requestMemberId);
        if (!writerId.equals(requestMemberId)) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_MEMBER);
        }
    }
}

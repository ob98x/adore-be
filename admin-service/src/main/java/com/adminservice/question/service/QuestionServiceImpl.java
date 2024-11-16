package com.adminservice.question.service;

import com.adminservice.global.*;
import com.adminservice.question.dto.GetQuestionListResponseDto;
import com.adminservice.question.dto.GetQuestionResponseDto;
import com.adminservice.question.entity.Question;
import com.adminservice.question.entity.QuestionCategory;
import com.adminservice.question.entity.QuestionState;
import com.adminservice.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public ResponseEntity<GetQuestionResponseDto> getQuestions(Long id) {
        log.info("[Question Service - getQuestions]: {}번 문의사항 조회 요청이 들어왔습니다.", id);
        return ResponseEntity.ok(GetQuestionResponseDto.createResponse(checkConflictQuestion(id)));
    }


    @Override
    public GetQuestionListResponseDto getQuestionList(SearchType searchType, FilterType filterType, QuestionCategory category, String searchKeyword, int page) {
        log.info("[Question Service - getQuestionList]: 문의사항 리스트 조회 요청이 들어왔습니다. page: {}, type: {}, filter: {}, category: {}, keyword: {}",
                page, searchType, filterType, category, searchKeyword);

        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        log.info("[Question Service - getQuestionList]: 검색 조건을 설정합니다.");

        Specification<Question> spec = Specification.where(null);

        if (searchType != null) {
            if (searchType == SearchType.NICKNAME) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("applicant").get("nickname"), "%" + searchKeyword + "%"));
            } else if (searchType == SearchType.EMAIL) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("applicant").get("email"), "%" + searchKeyword + "%"));
            }
        } else {
            spec = spec.and((root, query, cb) ->
                    cb.notEqual(root.get("state"), QuestionState.INACTIVE));
        }

        // 필터 조건 추가
        if (filterType != null ) {
            if (filterType == FilterType.WAIT) {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), QuestionState.WAIT));
            }
            else if (filterType == FilterType.COMPLETE) {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("state"), QuestionState.COMPLETE));
            } else {
                spec = spec.and( (root, query, cb) ->
                        cb.notEqual(root.get("state"), QuestionState.INACTIVE));
            }
        } else {
            spec = spec.and( (root, query, cb) ->
                    cb.notEqual(root.get("state"), QuestionState.INACTIVE));
        }

        if (category != null ) {
            if (category == QuestionCategory.SERVICE) {
                spec = spec.and( (root, query, cb) ->
                        cb.equal(root.get("category"), QuestionCategory.SERVICE));
            }
            else if (category == QuestionCategory.COMMENT) {
                spec = spec.and( (root, query, cb) ->
                        cb.equal(root.get("category"), QuestionCategory.COMMENT));
            }
            else if (category == QuestionCategory.USER) {
                spec = spec.and( (root, query, cb) ->
                        cb.equal(root.get("category"), QuestionCategory.USER));
            }
            else if (category == QuestionCategory.REVIEW) {
                spec = spec.and( (root, query, cb) ->
                        cb.equal(root.get("category"), QuestionCategory.REVIEW));
            }
            else if (category == QuestionCategory.ETC) {
                spec = spec.and( (root, query, cb) ->
                        cb.equal(root.get("category"), QuestionCategory.ETC));
            }
        } else {
            spec = spec.and( (root, query, cb) ->
                    cb.notEqual(root.get("state"), QuestionState.INACTIVE));
        }

        log.info("[Question Service - getQuestionList]: 문의사항 리스트를 DB 에서 가져옵니다.");
        Page<Question> resultPage = questionRepository.findAll(spec, pageable);

        log.info("[Question Service - getQuestionList]: 문의사항 리스트를 DTO 로 변환합니다.");
        List<GetQuestionListResponseDto.QuestionListInfo> questionList = resultPage.getContent().stream()
                .map(GetQuestionListResponseDto.QuestionListInfo::fromQuestion)
                .toList();

        return GetQuestionListResponseDto.createResponse(questionList, resultPage.getTotalPages(), resultPage.hasNext());
    }

    @Override
    public ResponseEntity<CustomResponseCode> processQuestions(Long id, String answerContent) {
        log.info("[Question Service - processQuestions]: {}번 문의사항 처리 요청이 들어왔습니다.", id);

        Question question = checkConflictQuestion(id);

        log.info("[Question Service - processQuestions]: 문의사항 처리를 진행합니다.");
        question.setState(QuestionState.COMPLETE);
        question.setAnswerContent(answerContent);
        questionRepository.save(question);
        return ResponseEntity.ok(CustomResponseCode.QUESTION_PROCESS_SUCCESS);
    }


    public Question checkConflictQuestion(Long id) {
        log.info("[Question Service - checkConflictQuestion]: 문의사항을 확인합니다. id: {}", id);
        if (questionRepository.findQuestionById(id).isEmpty()) {
            log.error("[Question Service - checkConflictQuestion]: 문의사항을 찾을 수 없습니다.");
            throw new CustomException(ResponseCode.QUESTION_NOT_FOUND);
        }

        if (questionRepository.findQuestionById(id).get().getState().equals(QuestionState.INACTIVE)) {
            log.error("[Question Service - checkConflictQuestion]: 삭제된 문의사항입니다.");
            throw new CustomException(ResponseCode.QUESTION_DELETED);
        }
        return questionRepository.findQuestionById(id).get();
    }
}

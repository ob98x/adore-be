package com.adminservice.question.service;

import com.adminservice.global.*;
import com.adminservice.question.dto.GetQuestionListResponseDto;
import com.adminservice.question.dto.GetQuestionResponseDto;
import com.adminservice.question.entity.Question;
import com.adminservice.question.entity.QuestionCategory;
import com.adminservice.question.entity.QuestionState;
import com.adminservice.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public ResponseEntity<GetQuestionResponseDto> getQuestions(Long id) {
        return ResponseEntity.ok(GetQuestionResponseDto.createResponse(checkConflictQuestion(id)));
    }


    @Override
    public List<GetQuestionListResponseDto.QuestionListInfo> allQuestions() {
        List<Question> questionList = questionRepository.findAll();
        return questionList.stream()
                .map(GetQuestionListResponseDto.QuestionListInfo::fromQuestion)
                .toList();
    }

    @Override
    public GetQuestionListResponseDto getQuestionList(SearchType searchType, FilterType filterType, QuestionCategory category, String searchKeyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        Specification<Question> spec = Specification.where(null);

        // 검색 조건 추가
        if (searchType != null) {
            if (searchType == SearchType.NICKNAME) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("applicant").get("nickname"), "%" + searchKeyword + "%"));
            } else if (searchType == SearchType.EMAIL) {
                spec = spec.and((root, query, cb) ->
                        cb.like(root.get("applicant").get("email"), "%" + searchKeyword + "%"));
            }
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
        }


        Page<Question> resultPage = questionRepository.findAll(spec, pageable);
        List<GetQuestionListResponseDto.QuestionListInfo> questionList = resultPage.getContent().stream()
                .map(GetQuestionListResponseDto.QuestionListInfo::fromQuestion)
                .toList();

        return GetQuestionListResponseDto.createResponse(questionList, resultPage.getTotalPages(), resultPage.hasNext());
    }

    @Override
    public ResponseEntity<CustomResponseCode> processQuestions(Long id, String answerContent) {
        Question question = checkConflictQuestion(id);
        question.setState(QuestionState.COMPLETE);
        question.setAnswerContent(answerContent);
        questionRepository.save(question);
        return ResponseEntity.ok(CustomResponseCode.QUESTION_PROCESS_SUCCESS);
    }


    public Question checkConflictQuestion(Long id) {
        // Check if the question exists
        if (questionRepository.findQuestionById(id).isEmpty()) {
            throw new CustomException(ResponseCode.QUESTION_NOT_FOUND);
        }

        // Check if the question is inactive
        if (questionRepository.findQuestionById(id).get().getState().equals(QuestionState.INACTIVE)) {
            throw new CustomException(ResponseCode.QUESTION_DELETED);
        }
        return questionRepository.findQuestionById(id).get();
    }
}

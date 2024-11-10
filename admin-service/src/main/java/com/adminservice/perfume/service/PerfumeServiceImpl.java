package com.adminservice.perfume.service;

import com.adminservice.global.CustomException;
import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.ResponseCode;
import com.adminservice.global.SearchType;
import com.adminservice.perfume.dto.GetPerfumeListResponseDto;
import com.adminservice.perfume.dto.GetPerfumeResponseDto;
import com.adminservice.perfume.dto.PerfumeCreateRequestDto;
import com.adminservice.perfume.entity.Perfume;
import com.adminservice.perfume.entity.PerfumeState;
import com.adminservice.perfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfumeServiceImpl implements PerfumeService {

    private final PerfumeRepository perfumeRepository;

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deletePerfume(Long id) {
        Perfume perfume = checkConflictPerfume(id);
        perfume.setState(PerfumeState.INACTIVE);
        perfumeRepository.save(perfume);
        return ResponseEntity.ok(CustomResponseCode.PERFUME_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createPerfume(PerfumeCreateRequestDto perfumeCreateRequestDto) {
        perfumeRepository.save(PerfumeCreateRequestDto.createPerfume(perfumeCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.PERFUME_CREATE_SUCCESS);

    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updatePerfume(Long id, PerfumeCreateRequestDto perfumeCreateRequestDto) {
        perfumeRepository.save(PerfumeCreateRequestDto.updatePerfume(checkConflictPerfume(id), perfumeCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.PERFUME_UPDATE_SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public GetPerfumeResponseDto getPerfume(Long id) {
        return GetPerfumeResponseDto.getPerfume(checkConflictPerfume(id));
    }

    @Override
    public List<GetPerfumeListResponseDto.PerfumeListInfo> allPerfumes() {
        List<Perfume> perfumes = perfumeRepository.findAll();
        return perfumes.stream()
                .map(GetPerfumeListResponseDto.PerfumeListInfo::fromPerfume)
                .toList();

    }

    // 전체 리스트
    public GetPerfumeListResponseDto searchPerfumes(SearchType searchType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.

        Specification<Perfume> spec = Specification.where(null);

        // 검색 타입에 따라 유저 검색
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

        Page<Perfume> resultPage = perfumeRepository.findAll(spec, pageable);
        List<GetPerfumeListResponseDto.PerfumeListInfo> perfumeList = resultPage.getContent().stream()
                .map(GetPerfumeListResponseDto.PerfumeListInfo::fromPerfume)
                .toList();

        return GetPerfumeListResponseDto.createResponse(perfumeList, resultPage.getTotalPages(), resultPage.hasNext());
    }


    public Perfume checkConflictPerfume(Long id) {
        if (perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).isEmpty()) {
            throw new CustomException(ResponseCode.PERFUME_NOT_FOUND);
        }
        if (perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).get().getState().equals(PerfumeState.INACTIVE)) {
            throw new CustomException(ResponseCode.PERFUME_DELETED);
        }
        return perfumeRepository.findByIdAndState(id, PerfumeState.ACTIVE).get();
    }
}

package com.userservice.survey.repository;

import com.userservice.survey.entity.FrRecommRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrRecommResRepository extends JpaRepository<FrRecommRes, Long>, JpaSpecificationExecutor<FrRecommRes> {
    List<FrRecommRes> findAllByFriendId(Long friendId);
    void deleteAllByFriendId(Long friendId);
}

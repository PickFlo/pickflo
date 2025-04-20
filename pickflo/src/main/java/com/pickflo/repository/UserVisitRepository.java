package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.UserVisit;

@Repository
public interface UserVisitRepository extends JpaRepository<UserVisit, Integer> {
  
    UserVisit findByUserId(Long id);
    
    // 홀수 user_id 재방문자 수 쿼리
    @Query("SELECT COUNT(u) FROM UserVisit u WHERE MOD(u.user.id, 2) = 1 AND u.isReturning = 'T'")
    Long countOddUserReturningVisits();

    // 짝수 user_id 재방문자 수 쿼리
    @Query("SELECT COUNT(u) FROM UserVisit u WHERE MOD(u.user.id, 2) = 0 AND u.isReturning = 'T'")
    Long countEvenUserReturningVisits();

    // 홀수 user_id 방문자 수 쿼리
    @Query("SELECT COUNT(u) FROM UserVisit u WHERE MOD(u.user.id, 2) = 1")
    Long countOddUserVisits();

    // 짝수 user_id 방문자 수 쿼리
    @Query("SELECT COUNT(u) FROM UserVisit u WHERE MOD(u.user.id, 2) = 0")
    Long countEvenUserVisits();
}

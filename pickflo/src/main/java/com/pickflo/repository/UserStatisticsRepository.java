package com.pickflo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickflo.domain.UserStatistics;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {

    // 특정 날짜에 대한 통계 조회
	@Query("SELECT us FROM UserStatistics us WHERE us.userGroup = :userGroup AND us.statDate = :statDate")
	List<UserStatistics> findByUserGroupAndStatDate(@Param("userGroup") String userGroup, @Param("statDate") LocalDate statDate);
	
	 // 날짜 범위에 따른 사용자 통계 조회
    @Query("SELECT us FROM UserStatistics us WHERE us.statDate BETWEEN :startDate AND :endDate")
    List<UserStatistics> findByStatDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
}
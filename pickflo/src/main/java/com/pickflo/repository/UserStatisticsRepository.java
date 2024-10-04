package com.pickflo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickflo.domain.UserStatistics;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long>{
	
	//UserStatistics findByUserGroup(String userGroup);
	
	// 사용자 그룹과 주 시작일로 통계 검색
    UserStatistics findByUserGroupAndWeekStartDate(String userGroup, LocalDate weekStartDate);

    
    @Query("SELECT us FROM UserStatistics us WHERE us.weekStartDate = :weekStartDate")
    List<UserStatistics> findByWeekStartDate(@Param("weekStartDate") LocalDate weekStartDate);

    
    List<UserStatistics> findAll();
}

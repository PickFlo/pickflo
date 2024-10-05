package com.pickflo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickflo.domain.UserStatistics;
import com.pickflo.dto.ChartDto;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {
	List<UserStatistics> findByUserGroup(String userGroup);

	List<UserStatistics> findAll();

	@Query("SELECT u.userGroup AS userGroup, " + "SUM(u.stayTime) AS totalStayTime, "
			+ "SUM(u.scrollCount) AS totalScrollCount, " + "SUM(u.likeCount) AS totalLikeCount, "
			+ "SUM(u.unlikeCount) AS totalUnlikeCount " + "FROM UserStatistics u "
			+ "WHERE u.statsDate BETWEEN :startDate AND :endDate " + "GROUP BY u.userGroup")
	 List<Object[]> findStatisticsByDateRange(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	/*
	 * @Query("SELECT u.statsDate, " +
	 * "SUM(CASE WHEN u.actionType = 'scroll' THEN u.scrollCount ELSE 0 END) AS totalScrolls, "
	 * +
	 * "SUM(CASE WHEN u.actionType = 'like' THEN u.likeCount ELSE 0 END) AS totalLikes, "
	 * +
	 * "SUM(CASE WHEN u.actionType = 'unlike' THEN u.unlikeCount ELSE 0 END) AS totalUnlikes "
	 * + "FROM UserStatistics u " +
	 * "WHERE u.statsDate BETWEEN :startDate AND :endDate " +
	 * "AND u.userGroup = :userGroup " + "GROUP BY u.statsDate " +
	 * "ORDER BY u.statsDate ASC") List<Object[]>
	 * getStatisticsByDateRangeAndGroup(@Param("startDate") LocalDate startDate,
	 * 
	 * @Param("endDate") LocalDate endDate,
	 * 
	 * @Param("userGroup") String userGroup);
	 */
}

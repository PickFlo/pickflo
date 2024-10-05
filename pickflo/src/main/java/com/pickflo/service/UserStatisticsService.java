package com.pickflo.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pickflo.domain.UserStatistics;
import com.pickflo.dto.ChartDto;
import com.pickflo.repository.UserStatisticsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatisticsService {

	private final UserStatisticsRepository userStatisticsRepo;

	public void saveUserData(ChartDto userData) {
	    LocalDate today = LocalDate.now(); // 오늘 날짜
	    // 사용자 그룹에 따른 통계 가져오기
	    List<UserStatistics> userStatisticsList = userStatisticsRepo.findByUserGroup(userData.getUserGroup());

	    UserStatistics userStatistics = null;

	    // 같은 날짜의 통계가 있는지 확인
	    for (UserStatistics stats : userStatisticsList) {
	        if (stats.getStatsDate() != null && stats.getStatsDate().isEqual(today)) {
	            userStatistics = stats; // 오늘 날짜의 통계가 있는 경우 해당 통계 사용
	            break;
	        }
	    }

	    // 오늘 날짜의 통계가 없으면 새로 생성
	    if (userStatistics == null) {
	        userStatistics = UserStatistics.builder()
	            .userGroup(userData.getUserGroup())
	            .stayTime(0)
	            .scrollCount(0)
	            .likeCount(0)
	            .unlikeCount(0)
	            .statsDate(today)
	            .lastUpdated(new Timestamp(System.currentTimeMillis()))
	            .build();
	    }

	    // actionType에 따라 이벤트 횟수 증가
	    switch (userData.getActionType()) {
	        case "stayTime":
	            userStatistics.setStayTime(userStatistics.getStayTime() + 1);
	            break;
	        case "scroll":
	            userStatistics.setScrollCount(userStatistics.getScrollCount() + 1);
	            break;
	        case "like":
	            if (userStatistics.getLikeCount() == null) {
	                userStatistics.setLikeCount(0);
	            }
	            userStatistics.setLikeCount(userStatistics.getLikeCount() + 1);
	            break;
	        case "unlike":
	            if (userStatistics.getUnlikeCount() == null) {
	                userStatistics.setUnlikeCount(0);
	            }
	            userStatistics.setUnlikeCount(userStatistics.getUnlikeCount() + 1);
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid action type: " + userData.getActionType());
	    }

	    // 마지막 업데이트 시간 갱신
	    userStatistics.setLastUpdated(new Timestamp(System.currentTimeMillis()));

	    // DB에 저장
	    userStatisticsRepo.save(userStatistics);
	}

	
	 public List<ChartDto> getStatisticsByDateRangeAndGroup(LocalDate startDate, LocalDate endDate, String userGroup) {
	        List<Object[]> rawStatistics = userStatisticsRepo.findStatisticsByDateRange(startDate, endDate);

	        List<ChartDto> chartData = new ArrayList<>();
	        for (Object[] rawStat : rawStatistics) {
	            LocalDate statsDate = (LocalDate) rawStat[0];
	            Long totalScrolls = (Long) rawStat[1]; 
	            Long totalLikes = (Long) rawStat[2];  
	            Long totalUnlikes = (Long) rawStat[3]; 

	            // 필요한 데이터로 ChartDto 생성
	            ChartDto scrollDto = new ChartDto(userGroup, "scroll", statsDate, totalScrolls);
	            ChartDto likeDto = new ChartDto(userGroup, "like", statsDate, totalLikes);
	            ChartDto unlikeDto = new ChartDto(userGroup, "unlike", statsDate, totalUnlikes);

	            // 리스트에 추가
	            chartData.add(scrollDto);
	            chartData.add(likeDto);
	            chartData.add(unlikeDto);
	        }

	        return chartData;
	    }

	public List<Object[]> getUserStatistics(LocalDate startDate, LocalDate endDate) {
	    return userStatisticsRepo.findStatisticsByDateRange(startDate, endDate);
	}
	
	public List<UserStatistics> getUserStatistics() {
		return userStatisticsRepo.findAll();
	}
	


}
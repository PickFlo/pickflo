package com.pickflo.service;

import com.pickflo.domain.UserStatistics;
import com.pickflo.repository.UserRepository;
import com.pickflo.repository.UserStatisticsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatisticsService {

    private final UserStatisticsRepository userStatisticsRepo;
    
    @Transactional
    public void incrementVisitCount(Long userId) {
        // 사용자 ID에 따라 유저 그룹을 결정
        String userGroup = (userId % 2 == 0) ? "B" : "A";

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // 오늘 해당 유저 그룹에 대한 통계 항목 조회
        UserStatistics statistics = userStatisticsRepo.findByUserGroupAndStatDate(userGroup, today)
                .stream().findFirst().orElseGet(() -> createNewStatistics(userGroup, today));
      
        statistics.setVisitorCount(statistics.getVisitorCount() + 1); // 방문자 수 증가
        statistics.setUpdatedAt(LocalDateTime.now());
        
        // 변경 사항 저장
        userStatisticsRepo.save(statistics);
    }
    


    @Transactional
    public void saveUserData(UserStatistics userData) {
        LocalDate today = LocalDate.now();
        UserStatistics statistics = userStatisticsRepo.findByUserGroupAndStatDate(userData.getUserGroup(), today)
                .stream().findFirst().orElseGet(() -> createNewStatistics(userData.getUserGroup(), today));

        statistics.setTimeSpent(statistics.getTimeSpent() + userData.getTimeSpent());
        statistics.setScrollCount(statistics.getScrollCount() + userData.getScrollCount());
        statistics.setLikeCount(statistics.getLikeCount() + userData.getLikeCount());
        statistics.setUnlikeCount(statistics.getUnlikeCount() + userData.getUnlikeCount());
        
        calculateConversionRate(statistics); // 전환율 계산

        statistics.setUpdatedAt(LocalDateTime.now()); // updatedAt 업데이트
        
        userStatisticsRepo.save(statistics); // 변경 사항 저장
    }

    
    // 전환율 계산
    private void calculateConversionRate(UserStatistics statistics) {
        if (statistics.getVisitorCount() > 0) {
            statistics.setConversionRate(((double) statistics.getLikeCount() / statistics.getVisitorCount()) * 100);
        } else {
            statistics.setConversionRate(0.0); // 방문자가 없을 경우 전환율 0으로 설정
        }
    }
    
    private UserStatistics createNewStatistics(String userGroup, LocalDate statDate) {
        return UserStatistics.builder()
                .userGroup(userGroup)
                .timeSpent(0)
                .scrollCount(0)
                .likeCount(0)
                .unlikeCount(0)
                .visitorCount(0)
                .conversionRate(0.0)
                .statDate(statDate)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public List<UserStatistics> getUserStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        return userStatisticsRepo.findByStatDateBetween(startDate, endDate);
    }
    
}
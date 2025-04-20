package com.pickflo.service;

import org.springframework.stereotype.Service;

import com.pickflo.domain.User;
import com.pickflo.domain.UserVisit;
import com.pickflo.repository.UserVisitRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserVisitService {

    private final UserVisitRepository userVisitRepo;

    @Transactional
    public void recordUserVisit(User user) {
    	
        // 유저의 방문 기록을 조회
        UserVisit userVisit = userVisitRepo.findByUserId(user.getId());

        if (userVisit == null) {
        		
            // 첫 로그인 시, 유저 방문 기록을 새로 생성
            userVisit = new UserVisit();
            userVisit.setUser(user);
            userVisit.setIsReturning("F"); // 기본값: 'F'
            userVisitRepo.save(userVisit);
            
        } else {
        	
            // 두 번째 로그인부터 재방문 여부를 'T'로 변경
            userVisit.setIsReturning("T");
            userVisitRepo.save(userVisit);
        }
    }
    
	 // 재방문율 계산 메소드
	    public double calculateOddUserReturnRate() {
	        Long oddVisits = userVisitRepo.countOddUserVisits();
	        Long oddReturningVisits = userVisitRepo.countOddUserReturningVisits();
	        return calculateReturnRate(oddVisits, oddReturningVisits);
	    }
	
	    
	    public double calculateEvenUserReturnRate() {
	        Long evenVisits = userVisitRepo.countEvenUserVisits();
	        Long evenReturningVisits = userVisitRepo.countEvenUserReturningVisits();
	        return calculateReturnRate(evenVisits, evenReturningVisits);
	    }
	
	    
	    private double calculateReturnRate(Long totalVisits, Long returningVisits) {
	        if (totalVisits == null || totalVisits == 0) {
	            return 0.0; // 방문자가 없을 경우 0% 재방문율
	        }
	        return (double) returningVisits / totalVisits * 100; // 재방문율 계산
	    }
	
}
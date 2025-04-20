package com.pickflo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.pickflo.domain.User;
import com.pickflo.service.UserMoviePickService;
import com.pickflo.service.UserService;
import com.pickflo.service.UserStatisticsService;
import com.pickflo.service.UserVisitService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final UserMoviePickService userMoviePickSvc;
	private final UserService userSvc;
	private final UserStatisticsService userStatisticsSvc;
	private final UserVisitService userVisitSvc;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
    		// 로그인한 사용자의 이메일을 통해 User 정보를 가져옴
        String email = authentication.getName();
        User user = userSvc.findByEmail(email);

        int pickedCount = userMoviePickSvc.getPickedCountByUserId(user.getId());

        log.info("User ID: {}", user.getId());
        log.info("Picked Count: {}", pickedCount);

        // userRole이 "member"인 경우에만 incrementVisitCount 호출
        if ("member".equals(user.getUserRole())) {
            userStatisticsSvc.incrementVisitCount(user.getId()); 
            userVisitSvc.recordUserVisit(user);
        }
    

        if (pickedCount < 3) {
            response.sendRedirect("/movie/picker");
        } else {
            response.sendRedirect("/");
        }
    }

}
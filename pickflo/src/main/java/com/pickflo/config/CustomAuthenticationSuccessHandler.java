package com.pickflo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.pickflo.domain.User;
import com.pickflo.service.UserMoviePickService;
import com.pickflo.service.UserService;

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
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 로그인한 사용자의 이메일을 통해 User 정보를 가져옴
        String email = authentication.getName();
        User user = userSvc.findByEmail(email);

        int pickedCount = userMoviePickSvc.getPickedCountByUserId(user.getId());

        log.info("User ID: {}", user.getId());
        log.info("Picked Count: {}", pickedCount);

        if (pickedCount < 3) {
            response.sendRedirect("/pickflo/movie/picker");
        } else {
            response.sendRedirect("/pickflo/");
        }
    }

}

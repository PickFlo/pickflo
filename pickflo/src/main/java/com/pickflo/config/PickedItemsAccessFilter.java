package com.pickflo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.pickflo.domain.User;
import com.pickflo.service.UserMoviePickService;
import com.pickflo.service.UserService;
import com.pickflo.service.UserStatisticsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PickedItemsAccessFilter extends GenericFilterBean {

	private final UserMoviePickService userMoviePickService;
	private final UserService userService;
	
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI(); // 요청 URI 가져오기
        log.info("requestURI={}", requestURI);
        
        // 현재 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) authentication.getPrincipal()).getUsername(); // 인증된 사용자의 이메일
            User user = userService.findByEmail(email); // 이메일로 사용자 정보 조회

            if (user != null) {
              // 사용자 역할 확인
                String userRole = user.getUserRole(); // User 엔티티에서 역할 가져오기
                int pickedCount = userMoviePickService.getPickedCountByUserId(user.getId()); // 사용자의 찜한 영화 수 조회

                log.info("pickedCount={}, referer={}", pickedCount, httpRequest.getHeader("Referer"));
                
                // 관리자와 일반 사용자 처리 로직 통합
                if ("admin".equals(userRole)) {
                    httpResponse.sendRedirect("/"); // 관리자도 일반 사용자 홈으로 리다이렉트
                } else {
                    // 일반 사용자 처리
                    if (pickedCount >= 3) {
                        if (requestURI.equals("/movie/picker")) {
                            httpResponse.sendRedirect("/"); // context root 페이지로
                        } else {
                            chain.doFilter(request, response); // 가던 곳 그대로
                        }
                    } else {
                        if (requestURI.equals("/movie/picker")) {
                            chain.doFilter(request, response); // 가던 곳 그대로
                        } else {
                            httpResponse.sendRedirect("/"); // context root 페이지로
                        }
                    }
                }
            } else {
                // 사용자 정보가 없는 경우 처리 (선택 사항)
                httpResponse.sendRedirect("/login"); // 로그인 페이지로 리디렉션
            }
        } else {
            // 인증 정보가 없는 경우 처리 (선택 사항)
            httpResponse.sendRedirect("/login"); // 로그인 페이지로 리디렉션
        }
    }
}
	
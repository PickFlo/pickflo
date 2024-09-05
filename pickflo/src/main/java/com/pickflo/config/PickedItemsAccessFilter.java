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
public class PickedItemsAccessFilter extends GenericFilterBean  {

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
                int pickedCount = userMoviePickService.getPickedCountByUserId(user.getId()); // 사용자의 찜한 영화 수 조회

                log.info("pickedCount={}, referer={}", pickedCount, httpRequest.getHeader("Referer"));
                
                if (pickedCount >= 3) {
                	if (requestURI.equals("/pickflo/movie/picker")) {
                		httpResponse.sendRedirect("/pickflo"); // context root 페이지로
                	} else {
                		chain.doFilter(request, response); // 가던 곳 그대로
                	}
                } else {
                	if (requestURI.equals("/pickflo/movie/picker")) {
                		chain.doFilter(request, response); // 가던 곳 그대로
                	} else {
                		httpResponse.sendRedirect("/pickflo"); // context root 페이지로 -> /pickflo/movie/picker로 다시 redirect
                	}
                }
                
            }
        }
        
        //chain.doFilter(request, response); // 필터 체인의 다음 필터로 요청 전달
    }
}
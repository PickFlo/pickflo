package com.pickflo.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pickflo.dto.CustomUserDetails;
import com.pickflo.service.UserMoviePickService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {
	
	private final UserMoviePickService userMoviePickSvc;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/")
	public String home() {
	    // SecurityContext에서 현재 로그인한 사용자 정보 가져오기
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	    // 로그인한 사용자의 userId를 얻기
	    Long userId = ((CustomUserDetails) userDetails).getId();

	    // 사용자 역할 확인
	    String userRole = ((CustomUserDetails) userDetails).getUserRole(); 

	    int pickedCount = userMoviePickSvc.getPickedCountByUserId(userId);
	    
	    /*
	    // 사용자 역할에 따라 리다이렉트
	    if ("admin".equals(userRole)) {
	        return "admin/home"; // 관리자 대시보드로 리다이렉트
	    } else {
	        if (pickedCount < 3) {
	            return "redirect:/movie/picker"; // 일반 사용자 홈으로 리다이렉트
	        } else {
	        		if (userId % 2 == 0) {
	        			return "home_B";
	        		} else {
	        			return "home_A"; // 일반 사용자 홈으로 리다이렉트
	        		}
	        }
	    }
	    */
	    
	    if ("admin".equals(userRole)) {
	        return "admin/home"; // 관리자 대시보드로 리다이렉트
	    } else {
	        if (pickedCount < 3) {
	            return "redirect:/movie/picker"; // 일반 사용자 홈으로 리다이렉트
	        } else {
	        		return "home";	
	        }
	    }
	    
	}
}
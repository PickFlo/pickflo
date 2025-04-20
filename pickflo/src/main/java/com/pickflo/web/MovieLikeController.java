package com.pickflo.web;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pickflo.domain.Movie;
import com.pickflo.domain.UserMoviePick;
import com.pickflo.dto.CustomUserDetails;
import com.pickflo.dto.UserMovieLikeDto;
import com.pickflo.service.MovieLikeService;
import com.pickflo.service.MovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/movie")
@Controller
@RequiredArgsConstructor
public class MovieLikeController {

	private final MovieLikeService moviceLikeSvc;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/like")
	public void moviesPage(Model model) {

		// SecurityContext에서 현재 로그인한 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		// 로그인한 사용자의 userId를 얻기
		Long userId = ((CustomUserDetails) userDetails).getId();

		// 사용자 ID로 영화 데이터를 가져오기
		List<UserMovieLikeDto> movies = moviceLikeSvc.getAllMovies(userId);

		// 모델에 영화 데이터 추가
		model.addAttribute("movies", movies);

	}
}

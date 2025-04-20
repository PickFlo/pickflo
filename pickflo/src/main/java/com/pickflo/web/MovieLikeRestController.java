package com.pickflo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.dto.UserMovieLikeDto;
import com.pickflo.service.MovieLikeService;

import com.pickflo.service.UserMoviePickService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieLikeRestController {

	private final MovieLikeService moviceLikeSvc;
	private final UserMoviePickService userMoviePickSvc;

	@GetMapping("/like-status")
	public Boolean showLikeStatus(@RequestParam Long userId, @RequestParam Long movieId) {
		boolean isFavorite = moviceLikeSvc.getLikeStatus(userId, movieId);
		return isFavorite;
	}

	@GetMapping("/like")
	public void likeMovie(@RequestParam Long userId, @RequestParam Long movieId) {
		userMoviePickSvc.addPick(userId, movieId);
	}

	@GetMapping("/unlike")
	public ResponseEntity<String> unlikeMovie(@RequestParam Long userId, @RequestParam Long movieId) {
		try {
			userMoviePickSvc.removePick(userId, movieId);
			// 정상적으로 삭제된 경우 응답
			return ResponseEntity.ok("success");
		} catch (IllegalStateException e) {
			// 영화 갯수가 3개 미만인 경우 "no" 응답을 보냅니다.
			return ResponseEntity.ok("no");
		}
	}

	// 영화 목록을 반환하는 엔드포인트
	@GetMapping("/updated-movie-list")
	public ResponseEntity<List<UserMovieLikeDto>> getUpdatedMovieList(@RequestParam Long userId) {
		try {
			List<UserMovieLikeDto> movieList = moviceLikeSvc.getAllMovies(userId);

			return ResponseEntity.ok(movieList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
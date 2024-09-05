package com.pickflo.Service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pickflo.dto.MovieDetailsDto;
import com.pickflo.repository.UserMoviePickRepository;
import com.pickflo.service.MovieService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class MovieServiceTest {

	@Autowired
	private MovieService movieSvc;

	@Autowired
	private UserMoviePickRepository userMoviePickRepo;

//	@Test
	public void testGetMovieDetails() {
		Long movieId = 189L;
		Optional<MovieDetailsDto> dto = movieSvc.getMovieDetails(movieId);

		if (dto.isPresent()) {
			log.info("Movie Details: {}", dto.toString());
		}
	}

//	@Test
	public void testExistsByUserIdAndMovieId() {
		Long userId = 21L; // 실제 사용자 ID
		Long movieId = 358L; // 실제 영화 ID

		// 테스트할 데이터베이스 상태에 맞게 userId와 movieId를 설정합니다.

		// 존재하는 경우 테스트
		boolean exists = userMoviePickRepo.existsByUserIdAndMovieId(userId, movieId);
		assertThat(exists).isTrue();

		// 존재하지 않는 경우 테스트
		exists = userMoviePickRepo.existsByUserIdAndMovieId(userId, 999L); // 존재하지 않는 영화 ID
		assertThat(exists).isFalse();
	}

}

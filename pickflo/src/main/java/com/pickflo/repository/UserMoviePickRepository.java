package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.pickflo.domain.UserMoviePick;
import com.pickflo.domain.UserMoviePickId;
import com.pickflo.dto.UserMovieLikeDto;

public interface UserMoviePickRepository extends JpaRepository<UserMoviePick, UserMoviePickId> {

	// 사용자가 찜한 항목의 갯수를 카운트하는 쿼리
	@Query("select count(ump) from UserMoviePick ump where ump.user.id = :userId")
	int countByUserId(@Param("userId") Long userId);

	// 찜 유무체크 확인하는 쿼리
	boolean existsByUserIdAndMovieId(Long userId, Long movieId);

	// 유저의 찜한 영화리스트
	@Query("SELECT new com.pickflo.dto.UserMovieLikeDto(ump.user.id, m.id, m.movieImg) "
			+ "FROM UserMoviePick ump JOIN ump.movie m " + "WHERE ump.user.id = :userId "
			+ "ORDER BY ump.createdTime DESC")
	List<UserMovieLikeDto> findByUserId(@Param("userId") Long userId);

	void deleteByUserIdAndMovieId(Long userId, Long movieId);

}

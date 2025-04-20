package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.Movie;
import com.pickflo.dto.MoviePickerDto;

import com.pickflo.dto.SearchGenreCountryDto;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieQuerydsl {
	// movieCode로 영화 존재 여부 확인
	boolean existsByMovieCode(Long movieCode);
    
    Movie findByMovieCode(Long movieCode);

    @Query("select new com.pickflo.dto.SearchGenreCountryDto(m.movieCode, m.movieImg) "
            + "from Movie m "
            + "join m.movieGenres mg "
            + "where mg.genre.id = :genreId")
    List<SearchGenreCountryDto> findMoviesByGenreId(@Param("genreId") Long genreId);
    
    /*
    @Query(value = "SELECT * FROM (" +
	            "    SELECT m.movie_id AS movieId, m.movie_title AS movieTitle, m.movie_img AS movieImg, " +
	            "           ROW_NUMBER() OVER (PARTITION BY g.genre_id ORDER BY DBMS_RANDOM.VALUE) AS rn " +
	            "    FROM movies m " +
	            "    JOIN movies_genres mg ON m.movie_id = mg.movie_id " +
	            "    JOIN genres g ON mg.genre_id = g.genre_id " +
	            "    WHERE m.movie_rating >= :rating " +
	            ") " +
	            "WHERE rn IN (:rn1, :rn2)", nativeQuery = true)
	List<MoviePickerDto> findMoviesByGenreAndRating(
	 @Param("rating") double rating, 
	 @Param("rn1") int rn1, 
	 @Param("rn2") int rn2
	);
*/
    @Query(value = "SELECT * FROM (" +
            "    SELECT m.movie_id AS movieId, m.movie_title AS movieTitle, m.movie_img AS movieImg, " +
            "           ROW_NUMBER() OVER (PARTITION BY g.genre_id ORDER BY RAND()) AS rn " +
            "    FROM movies m " +
            "    JOIN movies_genres mg ON m.movie_id = mg.movie_id " +
            "    JOIN genres g ON mg.genre_id = g.genre_id " +
            "    WHERE m.movie_rating >= :rating " +
            ") AS subquery " +
            "WHERE rn IN (:rn1, :rn2)", nativeQuery = true)
	List<MoviePickerDto> findMoviesByGenreAndRating(
	@Param("rating") double rating, 
	@Param("rn1") int rn1, 
	@Param("rn2") int rn2
	);




}
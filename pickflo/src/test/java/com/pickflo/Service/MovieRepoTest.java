package com.pickflo.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pickflo.domain.Movie;
import com.pickflo.repository.MovieRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MovieRepoTest {


    @Autowired
    private MovieRepository movieRepository;
    
    //@Test
	/*
	 * public void testFindMoviesByGenreWithRatingGreaterThanEqualSix() {
	 * List<Movie> movies =
	 * movieRepository.findMoviesByGenreWithRatingGreaterThanEqualSix();
	 * assertThat(movies).isNotEmpty(); assertThat(movies.size()).isEqualTo(2); //
	 * Assuming there are two movies with rating >= 6
	 * 
	 * // Additional assertions can be done to check the specific details of the
	 * movies assertThat(movies.get(0).getMovieTitle()).isEqualTo("Movie 1");
	 * assertThat(movies.get(1).getMovieTitle()).isEqualTo("Movie 2"); }
	 * 
	 * //@Test void test() { List<Movie> list=
	 * movieRepository.findMoviesByGenreWithRatingGreaterThanEqualSix();
	 * log.info("^^^^^^^^^^^^^^list= {}",list);
	 * 
	 * }
	 */
    //@Test
    void testFindMoviesByGenreAndRating() {
        Pageable pageable = PageRequest.of(0, 10);
        Long genreId = 1L;
        double rating = 6.0;

        // 메소드 호출
        //Page<Movie> moviesPage = movieRepository.findMoviesByGenreAndRating(pageable, genreId, rating);
        //List<Movie> movies = moviesPage.getContent();

        //log.info("&&&&&&&&&&&&&&&&movies= {}",movies);
        // 결과 검증
        //assertEquals(2, movies.size(), "Should return 2 movies");
        //assertEquals("Movie 1", movies.get(0).getMovieTitle(), "First movie title should be 'Movie 1'");
        //assertEquals("Movie 2", movies.get(1).getMovieTitle(), "Second movie title should be 'Movie 2'");
    }
}

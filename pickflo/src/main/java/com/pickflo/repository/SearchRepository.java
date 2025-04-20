package com.pickflo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickflo.domain.Movie;
import com.pickflo.dto.SearchGenreCountryDto;

public interface SearchRepository extends JpaRepository<Movie, Long>, SearchQuerydsl {
	
	@Query("select new com.pickflo.dto.SearchGenreCountryDto(m.id, m.movieImg) "
	        + "from Movie m "
	        + "where m.id in ("
	        + "    select m2.id "
	        + "    from Movie m2 "
	        + "    join MovieGenre mg on m2.id = mg.movieId "
	        + "    join Genre g on mg.genreId = g.id "
	        + "    join MovieCountry mc on m2.id = mc.movieId "
	        + "    join Country c on mc.countryId = c.id "
	        + "    where (:genreCode is null or g.genreCode = :genreCode) "
	        + "    and (:countryCode is null or c.countryCode = :countryCode)"
	        + ")")
	Page<SearchGenreCountryDto> findMoviesByGenreAndCountryCode(@Param("genreCode") Integer genreCode,
	                                                            @Param("countryCode") String countryCode,
	                                                            Pageable pageable);

	
	@Query("select distinct m "
			+ "from Movie m "
			+ "left join MoviePerson mp on m.id = mp.movieId "
			+ "left join Person p on mp.personId = p.id "
			+ "where upper(m.movieTitle) like upper('%' || :keywords || '%') "
			+ "or upper(p.personName) like upper('%' || :keywords || '%') ")

	List<Movie> findByMovieTitleOrPersonName(@Param("keywords") String keywords);

}

/*
@Query("select distinct new com.pickflo.dto.SearchGenreCountryDto(m.id, m.movieImg) "
        + "from Movie m "
        + "where m.id in ("
        + "    select distinct m2.id "
        + "    from Movie m2 "
        + "    join MovieGenre mg on m2.id = mg.movieId "
        + "    join Genre g on mg.genreId = g.id "
        + "    join MovieCountry mc on m2.id = mc.movieId "
        + "    join Country c on mc.countryId = c.id "
        + "    where (:genreCode is null or g.genreCode = :genreCode) "
        + "    and (:countryCode is null or c.countryCode = :countryCode)"
        + ")")
Page<SearchGenreCountryDto> findMoviesByGenreAndCountryCode(@Param("genreCode") Integer genreCode,
                                                            @Param("countryCode") String countryCode,
                                                            Pageable pageable);
                                                            
*/
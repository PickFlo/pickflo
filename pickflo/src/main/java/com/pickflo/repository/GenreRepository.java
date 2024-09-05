package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickflo.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	Genre findByGenreCode(Integer genreCode);
	
	@Query("select g.id from Genre g where g.genreCode = :genreCode")
    Long findGenreIdByCode(@Param("genreCode") Long genreCode);
}

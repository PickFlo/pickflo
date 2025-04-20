package com.pickflo.repository;

import com.pickflo.dto.MovieDetailsDto;

import java.util.Optional;

public interface MovieQuerydsl {
    Optional<MovieDetailsDto> findMovieDetailsById(Long movieId);
}
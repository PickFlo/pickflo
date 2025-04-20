package com.pickflo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.dto.MoviePickerDto;
import com.pickflo.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoviePickerService {

	private final MovieRepository repo;

	@Transactional(readOnly = true)
	public List<MoviePickerDto> readMoviesByGenreAndRating(double rating, int rn1, int rn2) {

		List<MoviePickerDto> result = repo.findMoviesByGenreAndRating(rating, rn1, rn2);

		return result;
	}

}

package com.pickflo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pickflo.domain.Movie;
import com.pickflo.dto.SearchGenreDto;
import com.pickflo.dto.SearchMovieListDto;
import com.pickflo.dto.SearchMovieRequestDto;
import com.pickflo.repository.SearchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {
	
	private final SearchRepository searchRepo;

	public List<SearchGenreDto> findMoviesByGenreAndCountryCode(Integer genreCode, String countryCode, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<SearchGenreDto> moviePage = searchRepo.findMoviesByGenreAndCountryCode(genreCode, countryCode, pageable);
        return moviePage.getContent();
    }
	
//	public List<SearchGenreDto> findMoviesByGenreAndCountryCode(Integer genreCode, String countryCode) {
//        return searchRepo.findMoviesByGenreAndCountryCode(genreCode, countryCode);
//	}
	
	public List<SearchMovieListDto> search(SearchMovieRequestDto dto) {
		log.info("SearchService(dto={})", dto);
		
		List<Movie> result = searchRepo.findByMovieTitleOrPersonName(dto.getKeywords());
		
		return result.stream()
				.map(SearchMovieListDto::fromEntity)
				.collect(Collectors.toList());
	}
	
}
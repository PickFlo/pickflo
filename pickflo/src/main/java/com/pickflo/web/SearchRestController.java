package com.pickflo.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.dto.SearchGenreCountryDto;
import com.pickflo.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchRestController {

	private final SearchService searchSvc;
	
	// 장르와 국가 코드로 영화 검색
	@GetMapping("/movies")
    public ResponseEntity<?> searchMoviesByGenreAndCountry(
            @RequestParam(required = false) Integer genreCode,
            @RequestParam(required = false) String countryCode,
            @RequestParam int page,
            @RequestParam int limit) {
        log.info("Received request for genreCode: {}, countryCode: {}, page: {}, limit: {}", genreCode, countryCode, page, limit);
        try {
            List<SearchGenreCountryDto> movies = searchSvc.findMoviesByGenreAndCountryCode(genreCode, countryCode, page, limit);
            if (!movies.isEmpty()) {
                return ResponseEntity.ok(movies);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found for the given criteria");
            }
        } catch (Exception e) {
            log.error("Error occurred while searching movies by genre and/or country: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
	

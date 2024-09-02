package com.pickflo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pickflo.dto.HomeRecMovieDto;
import com.pickflo.repository.HomeRecMovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
	
	private final HomeRecMovieRepository homeRecMovieRepo;
	
	
	public List<HomeRecMovieDto> getMoviesByUserId(Long userId) {
        List<Object[]> results = homeRecMovieRepo.findMoviesByUserId(userId);

        return results.stream()
            .map(result -> new HomeRecMovieDto(
                ((Number) result[0]).longValue(),
                (String) result[1] 
            ))
            .collect(Collectors.toList());
    }
	
	/*
	public List<HomeRecMovieDto> getMoviesByUserId(Long userId, int page, int limit) {
		
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			Page<Object[]> results = homeRecMovieRepo.findMoviesByUserId(userId, pageable);
	
	        return results.stream()
	            .map(result -> new HomeRecMovieDto(
	                ((Number) result[0]).longValue(),
	                (String) result[1] 
	            ))
	            .collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
	        throw new RuntimeException("Error retrieving movies", e);
		}
    } */
	
}

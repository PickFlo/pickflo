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
	
	public List<HomeRecMovieDto> getMoviesByUserId(Long userId, int page, int limit) {
        int startRow = (page - 1) * limit + 1;
        int endRow = page * limit;

        List<Object[]> results = homeRecMovieRepo.findMoviesByUserId(userId, startRow, endRow);

        return results.stream()
            .map(result -> new HomeRecMovieDto(
                ((Number) result[0]).longValue(),  
                (String) result[1],                
                (String) result[2]                
            ))
            .collect(Collectors.toList());
    }
	
}

/*
public List<HomeRecMovieDto> getMoviesByUserId(Long userId) {
    List<Object[]> results = homeRecMovieRepo.findMoviesByUserId(userId);

    return results.stream()
        .map(result -> new HomeRecMovieDto(
            ((Number) result[0]).longValue(),  
            (String) result[1],                
            (String) result[2]                
        ))
        .collect(Collectors.toList());
} 

}
*/
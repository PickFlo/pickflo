package com.pickflo.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.pickflo.dto.HomeRecMovieDto;
import com.pickflo.repository.HomeRecMovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
    
    private final HomeRecMovieRepository homeRecMovieRepo;
    
    public List<HomeRecMovieDto> getMoviesByUserIdAndGenres(Long userId, int startRow, int pageSize) {
        List<Object[]> results = homeRecMovieRepo.findMoviesByUserIdAndGenres(userId, startRow, pageSize);

        return results.stream()
            .map(result -> new HomeRecMovieDto(
                ((Number) result[0]).longValue(),  
                (String) result[1],                
                (String) result[2]                
            ))
            .collect(Collectors.toList());
    }
    
    public List<HomeRecMovieDto> getMoviesByUserIdAndPeople(Long userId, int startRow, int pageSize) {
        List<Object[]> results = homeRecMovieRepo.findMoviesByUserIdAndPeople(userId, startRow, pageSize);
        
        return results.stream()
            .map(result -> new HomeRecMovieDto(
                ((Number) result[0]).longValue(),
                (String) result[1],
                (String) result[2]
            ))
            .collect(Collectors.toList());
    }
}

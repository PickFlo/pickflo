package com.pickflo.web;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.dto.CustomUserDetails;
import com.pickflo.dto.HomeRecMovieDto;
import com.pickflo.service.HomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recMovies")
public class HomeRestController {
    
    private final HomeService homeSvc;    
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home_A")
    public List<HomeRecMovieDto> homeRecMoviesA(  
            @RequestParam int startRow, 
            @RequestParam int pageSize) { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = ((CustomUserDetails) userDetails).getId();

        return homeSvc.getMoviesByUserIdAndGenres(userId, startRow, pageSize); // endRow를 pageSize로 변경
    } 
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home_B")
    public List<HomeRecMovieDto> homeRecMoviesB(  
            @RequestParam int startRow, 
            @RequestParam int pageSize) { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = ((CustomUserDetails) userDetails).getId();

        return homeSvc.getMoviesByUserIdAndPeople(userId, startRow, pageSize); // endRow를 pageSize로 변경
    } 
}

package com.pickflo.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class MovieDetailsDto {
    private Long movieId;
    private String movieTitle;
    private String movieOverview;
    private Double movieRating;
    private LocalDate movieReleaseDate;
    private Integer movieRuntime;
    private String movieImg;
    private List<String> genres;
    private List<String> countries;
    private List<PersonDto> people;

    public MovieDetailsDto(Long movieId, String movieTitle, String movieOverview,
    		Double movieRating, LocalDate movieReleaseDate,
    		Integer movieRuntime, String movieImg) {      
                           
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.movieRating = movieRating;
        this.movieReleaseDate = movieReleaseDate;
        this.movieRuntime = movieRuntime;
        this.movieImg = movieImg;
    }
    
    @Data
    public static class PersonDto {
        private String personName;
        private String job;
        
        public PersonDto(String personName, String job) {
            this.personName = personName;
            this.job = job;
        }
    }
}
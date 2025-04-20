package com.pickflo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HomeRecMovieDto {
	private Long movieId;
	private String movieTitle;
	private String movieImg;
	// private LocalDate movieReleaseDate;
}

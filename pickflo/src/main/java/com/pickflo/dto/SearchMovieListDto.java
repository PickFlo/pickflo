package com.pickflo.dto;

import com.pickflo.domain.Movie;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SearchMovieListDto {

	private Long id;
	private Long movieCode;
	private String movieImg;
	
	public static SearchMovieListDto fromEntity(Movie entity) {
		return SearchMovieListDto.builder()
				.id(entity.getId())
				.movieCode(entity.getMovieCode())
				.movieImg(entity.getMovieImg())
				.build();
	}
}

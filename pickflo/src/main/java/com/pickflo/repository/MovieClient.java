package com.pickflo.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonProperty;

@FeignClient(name = "MovieClient", url = "${tmdb.api.url}") // 기본 URL
public interface MovieClient {

	@GetMapping("/discover/movie") // 기본 URL 뒤에 붙일 end point(장르별 영화)
	TmdbResponse getGenreMovies(@RequestParam("api_key") String apiKey, @RequestParam String with_genres,
			@RequestParam String language, @RequestParam String page);

	@GetMapping("/discover/movie") // 기본 URL 뒤에 붙일 end point
	TmdbResponse getCountryMovies(@RequestParam("api_key") String apiKey, @RequestParam String with_origin_country,
			@RequestParam String language, @RequestParam String page);

	@GetMapping("/movie/{id}") // 기본 URL 뒤에 붙일 end point(영화 상세)
	MovieDetailResponse getMovie(@RequestParam("api_key") String apiKey, @PathVariable Long id,
			@RequestParam String language);

	@GetMapping("/configuration/countries") // 기본 URL 뒤에 붙일 end point(국가)
	List<CountryData> getCountry(@RequestParam("api_key") String apiKey, @RequestParam String language);

	@GetMapping("/movie/{id}/credits") // 기본 URL 뒤에 붙일 end point(영화 출연진 및 제작진)
	MovieCreditsResponse getMoviePerson(@RequestParam("api_key") String apiKey, @PathVariable Long id,
			@RequestParam String language);

	// tmdb api의 영화 목록 필드 results
	class TmdbResponse {

		@JsonProperty("results")
		private List<MovieIdResponse> results;

		public List<MovieIdResponse> getResults() {
			return results;
		}

		public void setResults(List<MovieIdResponse> results) {
			this.results = results;
		}
	}

	class MovieIdResponse {
		private Long id;

		public Long getId() {
			return id;
		}
	}

	class MovieDetailResponse { // 영화 상세 정보에 대한 응답 클래스
		private Long id;
		private String title;
		private String poster_path;
		private String overview;
		private Double vote_average;
		private String release_date;
		private Integer runtime;
		
		@JsonProperty("adult")
	    private boolean adult; 
		
		@JsonProperty("vote_count")
	    private int voteCount;
		
		@JsonProperty("genres")
		private List<Genre> genres;

		@JsonProperty("origin_country")
		private List<String> originCountry;

		public Long getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getPoster_path() {
			return poster_path;
		}

		public String getOverview() {
			return overview;
		}

		public Double getVote_average() {
			return vote_average;
		}

		public String getRelease_date() {
			return release_date;
		}

		public Integer getRuntime() {
			return runtime;
		}
		
		public boolean isAdult() { 
	        return adult;
	    }
		
		public int getVoteCount() { 
	        return voteCount;
	    }
		
		public List<Genre> getGenres() {
			return genres;
		}

		public List<String> getOriginCountry() {
			return originCountry;
		}

	}

	class Genre {
		private Integer id;
		private String name;

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	class CountryData {
		private String iso_3166_1;
		private String native_name;

		public String getIso_3166_1() {
			return iso_3166_1;
		}

		public String getNative_name() {
			return native_name;
		}
	}

	class MovieCreditsResponse {
		// 배우
		@JsonProperty("cast")
		private List<PersonData> cast;
		// 감독
		@JsonProperty("crew")
		private List<PersonData> crew;
		
		public List<PersonData> getCast() {
			return cast;
		}

		public List<PersonData> getCrew() {
			return crew;
		}
	}

	class PersonData {
		private String known_for_department;
		private String job;
		private String name;
		// 인기도
		@JsonProperty("popularity") 
		private double popularity;
			
		public String getKnown_for_department() {
			return known_for_department;
		}

		public String getName() {
			return name;
		}
		
		public double getPopularity() {
			return popularity;
		}
		
		public String getJob() {
			return job;
		}
	}
}

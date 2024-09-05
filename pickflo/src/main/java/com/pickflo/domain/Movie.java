package com.pickflo.domain;
import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "MOVIES", uniqueConstraints = { @UniqueConstraint(columnNames = "movieCode") })
public class Movie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MOVIE_ID")
	private Long id;
	
	@Column(unique = true)
	@Basic(optional = false)
	private Long movieCode;
	
	@Basic(optional = false)
	private String movieTitle;
	
	private String movieOverview;
	
	@Basic(optional = false)
	private Double movieRating;
	
	private LocalDate movieReleaseDate;
	
	@Basic(optional = false)
	private Integer movieRuntime;
	
	private String movieImg;
	
	@OneToMany(mappedBy = "movie")
    private Set<MovieGenre> movieGenres;
	
	@OneToMany(mappedBy = "movie")
	private Set<MovieCountry> movieCountries;
	
	@OneToMany(mappedBy = "movie")
	private Set<MoviePerson> moviePeople;
	
}

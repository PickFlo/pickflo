package com.pickflo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "MOVIES_COUNTRIES")
@IdClass(MovieCountryId.class)
public class MovieCountry {
	
	@Id
	@Column(name = "MOVIE_ID")
	private Long movieId;
	
	@Id
	@Column(name = "COUNTRY_ID")
	private Long countryId;
	
	@ManyToOne
    @JoinColumn(name = "MOVIE_ID", insertable = false, updatable = false)
    private Movie movie;
    
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID", insertable = false, updatable = false)
    private Country country;
	
}

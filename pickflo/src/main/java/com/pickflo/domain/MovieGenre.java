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
@Table(name = "MOVIES_GENRES")
@IdClass(MovieGenreId.class)
public class MovieGenre {
	
	@Id
	@Column(name = "MOVIE_ID")
	private Long	 movieId;
	
	@Id
	@Column(name = "GENRE_ID")
	private Long genreId;
	
	@ManyToOne
    @JoinColumn(name = "MOVIE_ID", insertable = false, updatable = false)
    private Movie movie;
    
    @ManyToOne
    @JoinColumn(name = "GENRE_ID", insertable = false, updatable = false)
    private Genre genre;

}

package com.pickflo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder @ToString
@EqualsAndHashCode
@Entity
@Table(name = "	MOVIES_PEOPLE")
@IdClass(MoviePersonId.class)
public class MoviePerson {

	@Id
	@Column(name="movie_id")
	private Long movieId;
	
	@Id
	@Column(name = "person_id")
	private Long personId;
	
	@Column(name = "movie_person_job")
	private String job;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id", insertable = false, updatable = false)
	private Movie movie;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id", insertable = false, updatable = false)
	private Person person;
}
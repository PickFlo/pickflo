package com.pickflo.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.Movie;
import com.pickflo.domain.QMovie;
import com.pickflo.domain.QMoviePerson;
import com.pickflo.domain.QPerson;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SearchQuerydslImpl extends QuerydslRepositorySupport
	implements SearchQuerydsl {
	
	public SearchQuerydslImpl() {
		super(Movie.class);
	}
	
	@Override
	public List<Movie> searchByKeywords(String[] keywords) {
		log.info("searchByKeywords={}", Arrays.asList(keywords));
		
		QMovie movie = QMovie.movie;
		QMoviePerson moviePerson = QMoviePerson.moviePerson;
		QPerson person = QPerson.person;
		
		JPQLQuery<Movie> query = from(movie)
				.leftJoin(movie.moviePeople, moviePerson)
				.leftJoin(moviePerson.person, person)
				.fetchJoin();
		
		BooleanBuilder builder = new BooleanBuilder();
		for (String keyword : keywords) {
			builder.or(movie.movieTitle.containsIgnoreCase(keyword)
					.or(person.personName.containsIgnoreCase(keyword)));
		}
				
		query.where(builder).orderBy(movie.movieReleaseDate.desc());
		return query.fetch();

	}
	
}
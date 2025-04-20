package com.pickflo.repository;

import com.pickflo.domain.*;
import com.pickflo.dto.MovieDetailsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MovieQuerydslImpl extends QuerydslRepositorySupport implements MovieQuerydsl {

    public MovieQuerydslImpl() {
        super(Movie.class);
    }

    @Override
    public Optional<MovieDetailsDto> findMovieDetailsById(Long movieId) {
        QMovie movie = QMovie.movie;
        QMovieGenre movieGenre = QMovieGenre.movieGenre;
        QGenre genre = QGenre.genre;
        QMovieCountry movieCountry = QMovieCountry.movieCountry;
        QCountry country = QCountry.country;
        QMoviePerson moviePerson = QMoviePerson.moviePerson;
        QPerson person = QPerson.person;

        JPAQuery<String> genreQuery = new JPAQuery<>(getEntityManager());
        List<String> genres = genreQuery
            .select(genre.genreName)
            .from(movieGenre)
            .leftJoin(movieGenre.genre, genre)
            .where(movieGenre.movie.id.eq(movieId))
            .fetch();

        JPAQuery<String> countryQuery = new JPAQuery<>(getEntityManager());
        List<String> countries = countryQuery
            .select(country.countryName)
            .from(movieCountry)
            .leftJoin(movieCountry.country, country)
            .where(movieCountry.movie.id.eq(movieId))
            .fetch();

        JPAQuery<MovieDetailsDto.PersonDto> peopleQuery = new JPAQuery<>(getEntityManager());
        List<MovieDetailsDto.PersonDto> people = peopleQuery
            .select(Projections.constructor(MovieDetailsDto.PersonDto.class,
                person.personName,
                moviePerson.job
            ))
            .from(moviePerson)
            .leftJoin(moviePerson.person, person)
            .where(moviePerson.movie.id.eq(movieId))
            .fetch();

        JPAQuery<MovieDetailsDto> movieDetailsQuery = new JPAQuery<>(getEntityManager());
        MovieDetailsDto movieDetails = movieDetailsQuery
            .select(Projections.constructor(MovieDetailsDto.class,
                movie.id,
                movie.movieTitle,
                movie.movieOverview,
                movie.movieRating,
                movie.movieReleaseDate,
                movie.movieRuntime,
                movie.movieImg
            ))
            .from(movie)
            .where(movie.id.eq(movieId))
            .fetchOne();

        if (movieDetails != null) {
            movieDetails.setGenres(genres);
            movieDetails.setCountries(countries);
            movieDetails.setPeople(people);
        }

        return Optional.ofNullable(movieDetails);
    }
}
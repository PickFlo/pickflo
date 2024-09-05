package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.MovieCountry;
import com.pickflo.domain.MovieCountryId;

public interface MovieCountryRepository extends JpaRepository<MovieCountry, MovieCountryId> {

}

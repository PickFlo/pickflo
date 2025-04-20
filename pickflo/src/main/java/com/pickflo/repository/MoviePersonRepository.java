package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.MoviePerson;
import com.pickflo.domain.MoviePersonId;

public interface MoviePersonRepository extends JpaRepository<MoviePerson,MoviePersonId>{

}

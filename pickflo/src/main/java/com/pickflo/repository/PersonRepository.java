package com.pickflo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	Optional<Person> findByPersonName(String personName);

}

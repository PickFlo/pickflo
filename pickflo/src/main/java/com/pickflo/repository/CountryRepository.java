package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

	boolean existsByCountryCode(String countryCode);
	
	Country findByCountryCode(String countryCode);
}

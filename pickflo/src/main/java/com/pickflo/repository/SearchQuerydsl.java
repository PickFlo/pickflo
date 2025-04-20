package com.pickflo.repository;

import java.util.List;

import com.pickflo.domain.Movie;

public interface SearchQuerydsl {
	
	List<Movie> searchByKeywords(String[] keywords);
	
}

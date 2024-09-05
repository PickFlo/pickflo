package com.pickflo.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MovieGenreId implements Serializable {
 
	private static final long serialVersionUID = 1L;
	
	private Long movieId;
    private Long genreId;
   
}

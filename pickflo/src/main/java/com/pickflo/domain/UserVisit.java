package com.pickflo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@Entity
@Table(name = "user_visits")
public class UserVisit {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer visitId;
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
	private User user; 
	
	@Column(name = "is_returning", nullable = false)
	private String isReturning; // 기본값:'F', 재방문:'T'

}

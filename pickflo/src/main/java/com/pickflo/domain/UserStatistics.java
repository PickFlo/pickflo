package com.pickflo.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_statistics")
@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @Setter
public class UserStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userGroup;
	private int stayTime;
	private int scrollCount;
	private Integer likeCount;
	private Integer unlikeCount;
    private Timestamp lastUpdated;
    
    @Column(name = "stats_date", insertable = false, updatable = false)
    private LocalDate statsDate;
    
    @Column(name = "stats_week", insertable = false, updatable = false)
    private Integer statsWeek;
    
}
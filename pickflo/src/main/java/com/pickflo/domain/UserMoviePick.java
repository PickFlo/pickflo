package com.pickflo.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "USERS_MOVIES")
@IdClass(UserMoviePickId.class)
public class UserMoviePick {

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Id
	@Column(name = "movie_id")
	private Long movieId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id", insertable = false, updatable = false)
	private Movie movie;

	@Column(name = "created_time")
	private LocalDateTime createdTime;

	@PrePersist
	protected void onCreate() {
		this.createdTime = LocalDateTime.now();
	}
}

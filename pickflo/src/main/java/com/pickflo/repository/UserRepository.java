package com.pickflo.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{


	Optional<User> findByEmail(String email);
	Optional<User> findByNickname(String nickname);
	Optional<User> findByPassword(String password);

	
}

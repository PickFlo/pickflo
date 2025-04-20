package com.pickflo.dto;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.pickflo.domain.User;

import lombok.Data;

@Data
public class UserSignupDto {

	private String email;
	private String password;
	private String nickname;
	private LocalDate birth;
	private Integer gender; // 0:남성 1:여성

	private String userRole = "member"; 
	
	public User toEntity(PasswordEncoder encoder) {
		return User.builder()
				.email(email)
				.password(encoder.encode(password)) 
				.nickname(nickname)
				.birth(birth)
				.gender(gender)
				.userRole(userRole)
				.build();
	}

}

package com.pickflo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

	// Spring Security 5 버전부터 비밀번호는 반드시 암호화를 해야만 함.
	// 만약 비밀번호를 암호화하지 않으면, HTTP 403(access denied, 접근 거부) 또는
	// HTTP 500(internal server error, 내부 서버 오류) 에러가 발새함.
	// 비밀번호를 암호화하는 객체를 스프링 컨테이너가 bean으로 관리해야 함.
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}

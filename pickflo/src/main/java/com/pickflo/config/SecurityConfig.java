package com.pickflo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
//-> 스프링 컨테이너에서 생성하고 관리하는 설정 컴포넌트.
//-> 스프링 컨테이너에서 필요한 곳에 의존성 주입을 해줌.
@EnableMethodSecurity
//-> 컨트롤러 메서드에서 인증(로그인), 권한 설정을 하기 위해서.
public class SecurityConfig {
	
	private final CustomAuthenticationSuccessHandler customAuthSuccessHandler;
//	private final PickedItemsAccessFilter pickedItemsAccessFilter;
	
	// 스프링 시큐리티 필터 체인 객체(bean)
	// 로그인/로그아웃, 인증 필터에서 필요한 설정을 구성.
	// - 로그인 페이지(뷰), 로그아웃 페이지 설정.
	// - 페이지 접근 권한(ADMIN, USER) 설정.
	// - 인증 설정(로그인 없이 접근 가능한 페이지 vs 로그인해야만 접근 가능한 페이지)
	
	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
	            .authorizeHttpRequests(requests -> 
	                requests.requestMatchers("/user/signin/**", "/user/signup/**", "/css/**", "/js/**", "/images/**", "/api/**").permitAll() // 로그인/회원가입 페이지와 정적 자원에 대한 접근 허용
	                        .requestMatchers("/movie/picker").authenticated() // /movie/picker 페이지 접근은 인증된 사용자만 허용
	                        .requestMatchers("/movie/like", "/movie/search", "/").authenticated() // /movie/like와 / 페이지 접근은 인증된 사용자만 허용                   
	                        .anyRequest().authenticated()) // 나머지 모든 요청도 인증된 사용자만 접근 가능
	            .formLogin(form -> form.loginPage("/user/signin")
	                                .usernameParameter("email") // 로그인 시 이메일을 사용자 이름으로 사용
	                                .passwordParameter("password") // 로그인 시 비밀번호 파라미터 설정
	                                .successHandler(customAuthSuccessHandler) // 로그인 성공 시 핸들러 지정
	                                .permitAll()) // 로그인 페이지는 누구나 접근 가능
	            .logout(logout -> logout.logoutUrl("/user/logout")
	                                    .logoutSuccessUrl("/user/signin")
	                                    .permitAll()) // 로그아웃 URL 설정 및 로그아웃 성공 후 이동 페이지 설정

	            ;

	        return http.build();
	    }
	}
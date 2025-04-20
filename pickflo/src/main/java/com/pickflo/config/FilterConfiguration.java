package com.pickflo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.pickflo.service.UserMoviePickService;
import com.pickflo.service.UserService;
import com.pickflo.service.UserStatisticsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FilterConfiguration implements WebMvcConfigurer {
	
	private final UserMoviePickService userMoviePickSvc;
	private final UserService userService;
	
	@Bean
	public FilterRegistrationBean<PickedItemsAccessFilter> filterRegistrionBean() {
		log.info("filterRegistrionBean()");
		
		FilterRegistrationBean<PickedItemsAccessFilter> filterRegistration = 
				new FilterRegistrationBean<PickedItemsAccessFilter>(new PickedItemsAccessFilter(userMoviePickSvc, userService));
		filterRegistration.addUrlPatterns("/movie/picker", "/movie/search", "/movie/like");
		
		return filterRegistration;
	}
 
}

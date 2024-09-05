package com.pickflo.Service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pickflo.repository.UserMoviePickRepository;
import com.pickflo.service.UserMoviePickService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class userServiceTest {

	@Autowired
	private UserMoviePickService userMoviePickSvc;

	//@Test
	public void testGetPickedCountByUserId() {	
		Integer pickedCount = userMoviePickSvc.getPickedCountByUserId(1L);
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@2count={}", pickedCount);
		assertThat(pickedCount).isEqualTo(2);
	}

}

package com.pickflo.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.domain.UserStatistics;
import com.pickflo.dto.ChartDto;
import com.pickflo.service.UserStatisticsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/chart")
@RequiredArgsConstructor
public class ChartRestController {

	private final UserStatisticsService svc;

	@PostMapping("/saveUserData")
	public ResponseEntity<Void> saveUserData(@RequestBody ChartDto userData) {
		// DB에 userData 저장하는 로직 구현
		svc.saveUserData(userData);
		return ResponseEntity.ok().build();
	}

	// 데이터를 가져오는 API
/*
	@GetMapping("/getUserData")
    public ResponseEntity<List<UserStatistics>> getUserData() {
        List<UserStatistics> data = svc.getUserStatistics();

        return ResponseEntity.ok(data);
    }
*/
	
	@GetMapping("/getUserData")
	public ResponseEntity<List<UserStatistics>> getUserData(@RequestParam String date) {
		LocalDate parsedDate = LocalDate.parse(date); // String을 LocalDate로 변환
	    List<UserStatistics> stats = svc.getUserStatisticsByDate(parsedDate);
	    log.info("stats");
	    return ResponseEntity.ok(stats);
	}

	
}

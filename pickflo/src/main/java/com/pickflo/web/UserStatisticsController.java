package com.pickflo.web;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.domain.UserStatistics;
import com.pickflo.service.UserStatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-statistics")
@RequiredArgsConstructor
public class UserStatisticsController {
	
	private final UserStatisticsService userStatisticsSvc;

	@PostMapping("/saveUserData")
    public ResponseEntity<Void> saveUserData(@RequestBody UserStatistics userData) {
        try {
            userStatisticsSvc.saveUserData(userData);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 예외 처리
            System.err.println("Error saving user data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
  
	@GetMapping("/getUserData")
    public ResponseEntity<List<UserStatistics>> getUserData(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<UserStatistics> statistics = userStatisticsSvc.getUserStatisticsByDateRange(start, end);
            return ResponseEntity.ok(statistics);
        } catch (DateTimeParseException e) {
            // 날짜 파싱 오류 처리
            System.err.println("Invalid date format: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // 일반적인 오류 처리
            System.err.println("Error fetching user data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
   


}

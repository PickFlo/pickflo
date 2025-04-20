package com.pickflo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.service.UserVisitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-visits")
public class UserVisitController {
	
	private final UserVisitService userVisitSvc;
	
	@GetMapping("/return-rate/odd")
	public double getOddUserReturnRate() {
		return userVisitSvc.calculateOddUserReturnRate(); 
	}
	
	@GetMapping("/return-rate/even")
	public double getEvenUserReturnRate() {
		return userVisitSvc.calculateEvenUserReturnRate(); 
	}
	
}

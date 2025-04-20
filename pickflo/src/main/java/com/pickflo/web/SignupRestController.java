package com.pickflo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignupRestController {
	
	private final UserService userSvc;
	
	@GetMapping("/check-email")
	@ResponseBody
	public ResponseEntity<String> checkEmail(@RequestParam(name = "email") String email) {
		boolean result = userSvc.checkEmail(email);
		if (result) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}
	}
	
	@GetMapping("/check-password")
	@ResponseBody
	public ResponseEntity<String> checkPassword(@RequestParam(name = "password") String password) {
		boolean result = userSvc.checkPassword(password);
		if(result) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}
	}
	
}

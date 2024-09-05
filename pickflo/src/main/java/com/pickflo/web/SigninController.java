package com.pickflo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickflo.dto.UserSignupDto;
import com.pickflo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SigninController {

	private final UserService userSvc;

	@GetMapping("/user/signin")
	public void signin() {

	}

	@GetMapping("/user/signup")
	public void signup() {

	}

	@PostMapping("/user/signup")
	public String signup(UserSignupDto dto, RedirectAttributes redirectAttributes) {
		try {
			userSvc.create(dto);
			redirectAttributes.addFlashAttribute("signupSuccess");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("signupError");
		}
		return "redirect:/movie/picker";
	}

}

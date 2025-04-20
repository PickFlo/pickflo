package com.pickflo.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/movie/picker")
public class MoviePickerController {

	@PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String pickerPage() {     
		// 정상적인 접근 시 picker 페이지로 이동
        return "movie/picker";
    }
}

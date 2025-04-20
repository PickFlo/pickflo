package com.pickflo.web;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pickflo.dto.SearchMovieListDto;
import com.pickflo.dto.SearchMovieRequestDto;
import com.pickflo.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SearchController {
	
	private final SearchService searchSvc;
	
	@GetMapping("/movie/search")
    public String search(@RequestParam(value = "keywords", required = false) String keywords,
                         Model model) {
        log.info("SearchController(keywords={})", keywords);
        
        SearchMovieRequestDto dto = new SearchMovieRequestDto();
        dto.setKeywords(keywords);
        
        if (keywords == null || keywords.trim().isEmpty()) {
            model.addAttribute("searchMovies", Collections.emptyList());
        } else {
            List<SearchMovieListDto> result = searchSvc.search(dto);
            model.addAttribute("searchMovies", result);
        }
        
        model.addAttribute("keywords", keywords); // 검색어를 유지하기 위해 추가
        
        return "movie/search";
    }
	
}

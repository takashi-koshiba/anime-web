package com.example.web.anime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class anime {

	@GetMapping("/anime-web/anime")
	public ModelAndView  start() {
		ModelAndView model= new ModelAndView("anime-web/anime/index");

		
		return model;
	}
	@GetMapping("/anime-web/anime/")
	public String  start1() {
		return "redirect:/anime-web/anime";
	}

}

package com.example.web.index;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class index {



	
	@GetMapping("/anime-web/")
	public ModelAndView start(HttpServletRequest request) {
		ModelAndView model= new ModelAndView("anime-web/index/index");
		BeanUser user =GetIP.GetNameAndIp(request);
		model.addObject("user",user);
		
		
		
		return model;
	}
	
	
	
	
	
	
    
}

package com.example.web.index;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class index {



	
	@RequestMapping("/")
	public ModelAndView start(HttpServletRequest request) {
		ModelAndView model= new ModelAndView("index/index");
		BeanUser user =GetIP.GetNameAndIp(request);
		model.addObject("user",user);
		
		
		
		return model;
	}
	
	
	
	
	
	
    
}

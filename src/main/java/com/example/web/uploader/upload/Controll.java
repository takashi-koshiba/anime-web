package com.example.web.uploader.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.etc.db.upload.UploadService;


@Controller
public class Controll {
	@Autowired
	UploadService uploadService;
	

    
	@GetMapping("/anime-web/uploader/login/create")
	public ModelAndView  start() {
		ModelAndView model= new ModelAndView("anime-web/uploader/create");

		
		return model;
	}
	@GetMapping("/anime-web/uploader/login/create/")
	public String  start1() {
		return "redirect:/anime-web/uploader/create";
	}
	
	
	@GetMapping("/anime-web/uploader/login/")
	public String  start3() {
		return "redirect:/anime-web/uploader/login";
	}
	@GetMapping("/anime-web/uploader/login")
	public ModelAndView  start4() {
		ModelAndView model= new ModelAndView("anime-web/uploader/login");
		model.addObject("users", uploadService.selectUserAll());

		return model;
	}
	

}

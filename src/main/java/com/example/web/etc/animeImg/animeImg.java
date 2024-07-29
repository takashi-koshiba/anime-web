package com.example.web.etc.animeImg;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.etc.sta.Kakasi;

@Controller
public class animeImg {
	@GetMapping("/etc/animeimg/")
	public ModelAndView  start() {
		ModelAndView model= new ModelAndView("etc/animeimg/index");

		Kakasi.main("a");
		 //return null;
		 
		
		return model;
	}
	
/*
	@PostMapping("/etc/animeimg/")
	public ModelAndView post(AnimeBean bean) {
		ModelAndView model= new ModelAndView("etc/animeimg/index");
		MultipartFile[] file=bean.getUpload();
		
		String[] fileName=new String[file.length];
		for(int i=0;i<file.length;i++) {
			fileName[i]=file[i].getOriginalFilename();
		}

		model.addObject("filename",fileName);
		model.addObject("file",file);
		return model;
	}
	*/

	
}

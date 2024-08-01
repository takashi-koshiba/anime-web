package com.example.web.etc.settings.addanime;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.index.BeanUser;
import com.example.web.index.GetIP;

@Controller
public class animeImg {
	@GetMapping("/etc/settings/addanime/")
	public ModelAndView  start(HttpServletRequest request) {
		BeanUser user =GetIP.GetNameAndIp(request);
		ModelAndView model;
		if(user.isRoot()) {
			model= new ModelAndView("etc/settings/addanime/index");
		}else {
			 model= new ModelAndView("etc/error/admin/index");
			
		}
		return model;
		
		
	}
	
	@GetMapping("/etc/settings/addanime")
	public String start2() {
		return "redirect:/etc/settings/addanime/";
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

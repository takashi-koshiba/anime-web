package com.example.web.etc.settings.setting;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.etc.sta.Setting;
import com.example.web.index.BeanUser;
import com.example.web.index.GetIP;

@Controller
public class SettingAdmin {
	@GetMapping("/anime-web/etc/settings/setting/")
	public ModelAndView  start(HttpServletRequest request) {
		
		ModelAndView model;
		BeanUser user =GetIP.GetNameAndIp(request);

		if(user.isAdmin()) {
			model= new ModelAndView("anime-web/etc/settings/setting/index");
			model.addObject("path",Setting.getRoot());

			model.addObject("videoPath",Setting.getVideoPath());
			model.addObject("encoder",Setting.getEncoder().ordinal());
			
		}else {
			 model= new ModelAndView("anime-web/etc/error/admin/index");
			
		}
		return model;
		
		
	}
	
	@GetMapping("/anime-web/etc/settings/setting")
	public String start2() {
		return "redirect:/anime-web/etc/settings/setting/";
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

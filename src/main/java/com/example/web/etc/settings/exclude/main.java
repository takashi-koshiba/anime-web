package com.example.web.etc.settings.exclude;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.etc.db.animeExclude.Exclude;
import com.example.web.etc.db.animeExclude.ExcludeService;
import com.example.web.index.BeanUser;
import com.example.web.index.GetIP;

@Controller
public class main {
	@Autowired
	ExcludeService  excludeService ;
	
	
	@GetMapping("/anime-web/etc/settings/exclude/")
	public ModelAndView  start(HttpServletRequest request) {
		BeanUser user =GetIP.GetNameAndIp(request);
		ModelAndView model;
		if(user.isAdmin()) {
			List<Exclude> excludeTitle=excludeService.selectAll();
			
			model= new ModelAndView("anime-web/etc/settings/exclude/index");
			model.addObject("excludeTitle", excludeTitle);
		}else {
			 model= new ModelAndView("anime-web/etc/error/admin/index");
			
		}
		return model;
		
		
	}
	
	@PostMapping("/anime-web/etc/settings/exclude/")

	public String  start2(HttpServletRequest request ,@ModelAttribute Form title) {
		BeanUser user =GetIP.GetNameAndIp(request);


		if(user.isAdmin()) {
			excludeService.del();
			insertExclude(title);
			
			return "redirect:/anime-web/etc/settings/exclude/";
		}
		return "redirect:/anime-web/etc/error/admin/index";
		
		
	}
	private void insertExclude(Form titles) {
	
		List<String> title = titles.getTitle();
		for(String t :title) {
			if(t.isEmpty()) {
				return;
			}
			excludeService.insert(t);
		}
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

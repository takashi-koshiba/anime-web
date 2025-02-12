package com.example.web.uploader;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.etc.db.upload.UploadService;


@Controller
public class Controll3 {
	@Autowired
	UploadService uploadService;
	
	
	private HttpSession session;
	public Controll3(HttpSession session) {
        // フィールドに代入する
    this.session = session;
}
	
	@GetMapping("/anime-web/uploader")
	public ModelAndView   start() {
		
		if(this.session.getAttribute("id")==null) {
			ModelAndView model= new ModelAndView("redirect:/anime-web/uploader/login");
			return model;
		}
		String  id=this.session.getAttribute("id").toString();
		ModelAndView model= new ModelAndView("anime-web/uploader/index");
		model.addObject("users", uploadService.selectOne(id));
		
		return model;
		
	}
	@GetMapping("/anime-web/uploader/")
	public String  start1() {
		return "redirect:/anime-web/uploader";
	}
	
 

	

}

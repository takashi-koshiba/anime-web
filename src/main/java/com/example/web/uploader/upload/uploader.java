package com.example.web.uploader.upload;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class uploader {
	private HttpSession session;
	 

	public uploader(HttpSession session) {
	        // フィールドに代入する
	    this.session = session;
	}
	 
	
	@GetMapping("/anime-web/uploader/upload")
	public String  start() {

		
		if(this.session.getAttribute("id")==null) {
			return "redirect:/anime-web/uploader/login";
		}

		return "anime-web/uploader/upload";
	}
	@GetMapping("/anime-web/uploader/upload/")
	public String  start1() {
		return "redirect:/anime-web/uploader/upload";
	}

}

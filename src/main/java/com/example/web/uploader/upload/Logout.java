package com.example.web.uploader.upload;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Logout {
	private HttpSession session;
	 

	public Logout(HttpSession session) {
	        // フィールドに代入する
	    this.session = session;
	}
	 
	
	@GetMapping("/anime-web/uploader/logout")
	public String  start() {
		session.removeAttribute("id");
		return "anime-web/uploader/logout";
	}
	@GetMapping("/anime-web/uploader/logout/")
	public String  start1() {
		return "redirect:/anime-web/uploader/logout";
	}

}

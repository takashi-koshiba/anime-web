package com.example.web.uploader.loginApi;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.upload.Upload;
import com.example.web.etc.db.upload.UploadService;
import com.example.web.etc.sta.ToHash256;

@RestController
public class Login {
	
	@Autowired
	UploadService uploadService;
	
	private HttpSession session;
	 

	public Login(HttpSession session) {
	        // フィールドに代入する
	    this.session = session;
	}
	 
	          
	@PostMapping("/anime-web/uploader/login")
	public Integer start2(@RequestParam Integer id, @RequestParam String pw) {

		if(pw==null||pw.equals("")) {
			
			return 1;//入力に問題あり
		}
		
		Upload user = uploadService.login(id, ToHash256.main(pw));

		if(user.getName() ==null) {
			return 2;
		}
		
		
		this.session.setAttribute("id", id);
		
		return 0;
	}
	
}
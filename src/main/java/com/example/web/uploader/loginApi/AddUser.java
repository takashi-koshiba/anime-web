package com.example.web.uploader.loginApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.upload.Upload;
import com.example.web.etc.db.upload.UploadService;
import com.example.web.etc.sta.ToHash256;

@RestController
public class AddUser {
	
	@Autowired
	UploadService uploadService;

	@PostMapping("/anime-web/uploader/login/addUser")
	public Integer start2(@RequestParam String name, @RequestParam String pw) {
		
		
		
		if(name==null||name.equals("")||pw==null||pw.equals("")) {
			
			return 1;//入力に問題あり
		}
		Integer result=3;//重複
		Upload animeInfo =uploadService.selectOne(name);
		if(animeInfo.getName() ==null) {
			Boolean flag;
			flag=uploadService.insertUser(name, ToHash256.main(pw));
			
			System.out.println( ToHash256.main(pw));
			result=flag?0:2; //問題なし/書き込み失敗
			
		}
		
		
		return result;
	}
	
}
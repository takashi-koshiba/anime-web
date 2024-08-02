package com.example.web.rest.settings.directory;


import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.sta.PathClass;
import com.example.web.etc.sta.Setting;
import com.example.web.index.BeanUser;
import com.example.web.index.GetIP;
@RestController

public class Folder {

	
	@PostMapping("/anime-web/api/directory/root/")
	public boolean start(@RequestPart("path") String path)  {
		
		return PathClass.IsExistFolder(path);
		
		
	}
	@PostMapping("/anime-web/api/directory/url/")
	public boolean start3(@RequestPart("url") String url)  {
		
		return Setting.IsUrl(url);
		
		
	}
	@PostMapping("/anime-web/api/change-directory/")
	public Integer start2(@RequestPart("path") String path,@RequestPart("url") String url,HttpServletRequest request) throws IOException{
		
		BeanUser user =GetIP.GetNameAndIp(request);
		Integer result;
		if(!PathClass.IsExistFolder(path)) {
			result=1;
		}else if(!user.isAdmin()) {
			result=2;
		}else if(!Setting.IsUrl(url)) {
			result=3;
		}
		else {
			try {
				Setting.setRoot(path.replace("/", "\\"));
				
				//フォルダを作成
				result=Setting.makeDirectory()? 0 : 2;
				
				Setting.setUrl(url);
			} catch (IOException e) {
				result=2;
				
			}
		}
		
		System.out.println(Setting.getRoot());
		return result;
		
		
		
	}
	
}

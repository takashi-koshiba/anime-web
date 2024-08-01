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

	
	@PostMapping("/api/directory/")
	public boolean start(@RequestPart("path") String path)  {
		
		return PathClass.IsExistFolder(path);
		
		
	}
	@PostMapping("/api/change-directory/")
	public Integer start2(@RequestPart("path") String path,HttpServletRequest request) throws IOException{
		
		BeanUser user =GetIP.GetNameAndIp(request);
		Integer result;
		if(!PathClass.IsExistFolder(path)) {
			result=1;
		}else if(!user.isRoot()) {
			result=2;
		}
		else {
			try {
				Setting.setRoot(path.replace("/", "\\"));
				
				//フォルダを作成
				result=Setting.makeDirectory()? 0 : 2;
			} catch (IOException e) {
				result=2;
				
			}
		}
		
		System.out.println(Setting.getRoot());
		return result;
		
		
		
	}
	
}

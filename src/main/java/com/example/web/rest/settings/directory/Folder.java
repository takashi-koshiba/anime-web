package com.example.web.rest.settings.directory;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

	
	@PostMapping("/anime-web/api/directory/is-exist/")
	public boolean start(@RequestPart("path") String path)  {
		
		return PathClass.IsExistFolder(path);
		
		
	}
	@PostMapping("/anime-web/api/directory/url/")
	public boolean start3(@RequestPart("url") String url)  {
		
		return Setting.IsUrl(url);
		
		
	}
	@PostMapping("/anime-web/api/change-directory/")
	public Integer start2(@RequestPart("path") String path,@RequestPart("videoPath") String videoPath,HttpServletRequest request) throws IOException{
		
		BeanUser user =GetIP.GetNameAndIp(request);
		Integer result;
		Path documentRoot=Paths.get(path).toAbsolutePath().normalize();
		Path videopath=Paths.get(videoPath).toAbsolutePath().normalize();
		
		System.out.println(documentRoot);
		if(!PathClass.IsExistFolder(documentRoot.toString())) {
			System.out.println("指定されたフォルダは存在しません"+documentRoot);
			result=1;
		}else if(!user.isAdmin()) {
			System.out.println("管理者以外のユーザーでアクセスがありました。");
			result=2;
		}else if(!PathClass.IsExistFolder(videopath.toString())) {
			System.out.println("指定された動画のディレクトリは存在しません"+videopath);
			result=4;
		}
		else {
			try {
				Setting.setRoot(documentRoot.toString());
				Setting.setVideoPath(videopath.toString());
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

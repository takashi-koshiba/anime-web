package com.example.web.rest.animeimg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.rest.animeimg.Animetable.Anime;
import com.example.web.rest.animeimg.Animetable.AnimeService;


@RestController

public class upload {

	
	@Autowired
	AnimeService animeService;
	
	@PostMapping("/api/upload")

	
	public String start(@RequestPart("img") String img,@RequestPart("folder") String folder,@RequestParam("foldername") String foldername,@RequestParam("originalName") String originalName,@RequestParam("extension") String extension  )  {
		
		//DBに存在するか
		boolean exist=animeService.IsExistItem(originalName);
		System.out.print(exist);
			
		if(!exist) {
			File f=new File(folder+"\\");
			
			if(f.exists()) {
				try{
					 //byte[] bytes = file.getBytes();
			         Path path = Paths.get(folder+"\\" + originalName+"."+extension);
			         
			         
			         byte[] imgBytes=Base64.getDecoder().decode(img);
			         Files.write(path, imgBytes);
			         
			         //
			         animeService.insert(new Anime(originalName,foldername));
			         
			         return "ok";
			         
				}catch(IOException e) {
				     return "アップロードに失敗しました。";
				}
			 }else {
				return "指定したフォルダが存在しません。";
			 }
			//String folder = "C:\\Users\\muu4\\Documents\\新しいフォルダー\\";
			
		
		}else {
			return "すでに登録されています。";
		}
		
	}
	
	
}

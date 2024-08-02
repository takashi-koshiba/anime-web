package com.example.web.rest.animeimg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.sta.Setting;
import com.example.web.etc.sta.TextRep;


@RestController

public class upload {

	
	@Autowired
	AnimeService animeService;
	
	@PostMapping("/anime-web/api/upload")

	
	public String start(@RequestPart("img") String img,@RequestParam("foldername") String foldername,@RequestParam("originalName") String originalName,@RequestParam("extension") String extension  )  {
		
		//DBに存在するか
		boolean exist=animeService.IsExistItem(originalName);

		
		//画像のアップロード先
        String fullPath = Setting.getRoot();
        String folder = fullPath+ "anime-web\\upload\\img\\temp\\";
		if(!exist) {
			File f=new File(folder+"\\");
			
			if(f.exists()) {
				try{
					
					 //Path p=Paths.get("");
					 //Path p2=p.toAbsolutePath();
					 //System.out.print(p2);
					 
					 //byte[] bytes = file.getBytes();
					
					//画像をアップロード
					String imgPath=folder+"\\" + originalName+"."+extension;
			         Path path = Paths.get(imgPath);
			         byte[] imgBytes=Base64.getDecoder().decode(img);
			         Files.write(path, imgBytes);
			         
			         foldername=TextRep.main(foldername);
			         
			         ffmpeg(originalName,extension);
			         
			         Setting.makeAnimeDirectory(foldername);
			         
			         Anime anime =new Anime();
			         anime.setOriginalName(originalName);
			         anime.setFoldername(foldername);
			         animeService.insert(anime);
			         
			         

			         
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

	//画像を縮小
	private void ffmpeg(String fname,String extension) throws IOException {
		String root = Setting.getRoot();
		String sourcePath=root+"anime-web\\upload\\img\\temp\\"+fname+"."+extension;
		String savePath=root+"anime-web\\upload\\img\\thumbnail\\"+fname+".webp";
		
		String cmd="ffmpeg -i {0}   -vf scale=320:-1 -compression_level 6 -quality 30 {1}" ;
	
		String format= MessageFormat.format(cmd,sourcePath,savePath);	
		 ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", format);

         // コマンドを実行
         processBuilder.start();

	}

	
	
}

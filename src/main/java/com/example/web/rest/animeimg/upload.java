package com.example.web.rest.animeimg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.animeVector.insert.AnimeInsertService;
import com.example.web.etc.sta.ExecProcessget;
import com.example.web.etc.sta.Kakasi;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.Setting;
import com.example.web.etc.sta.TextRep;


@RestController

public class upload {

	
	@Autowired
	AnimeService animeService;
	
	@Autowired 
	AnimeInsertService animeInsertService;
	
	@PostMapping("/anime-web/api/upload")

	
	public List<String> start(@RequestPart("img") String img,@RequestParam("foldername") String foldername,@RequestParam("originalName") String originalName,@RequestParam("extension") String extension  )  {
		
		//DBに存在するか
		boolean exist=animeService.IsExistItem(originalName);
		List<String > r= new ArrayList<>();
		
		
		//画像のアップロード先
        String fullPath = Setting.getRoot();
        String folder = fullPath+ "content\\anime-web\\upload\\img\\temp\\";
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
			      
			         foldername=Kakasi.main(TextRep.main(foldername,false),"-JH -KH");
			         
			      
			         ffmpeg(originalName,path);
			     
			         
			         Setting.makeAnimeDirectory(foldername);
			         
			         Anime anime =new Anime();
			         anime.setOriginalName(originalName);
			         anime.setFoldername(TextRep.main(Kakasi.main(foldername,"-JH -KH"),false));
			         animeService.insert(anime);
			         
			         animeInsertService.insertTitle(-1);
			         
			         r.add("ok");
			         
			         return r;
			         
				}catch(IOException e) {
					String str="アップロード処理に失敗しました。";
					Log.detail(Level.WARNING, str, e);
					r.add(str);
					return r;
				     
				}catch(MaxUploadSizeExceededException e) {
					String str="ファイルサイズが大きすぎます";
					Log.detail(Level.WARNING, str, e);
					r.add(str);
					return r;
				}
				catch(Exception e) {
					String str="未知のエラー";
					Log.detail(Level.WARNING, str, e);
					r.add(str);
					return r;
				}
				
			 }else {
					String str="指定したフォルダが存在しません。";
					Log.log(Level.WARNING, str);
					
					r.add(str);
					return r;
			 }
			//String folder = "C:\\Users\\muu4\\Documents\\新しいフォルダー\\";
			
		
		}else {
			String str="すでに登録されています。";
			Log.log(Level.WARNING, str+": "+originalName);
			r.add(str);
			return r;
		}
		
	}

	//画像を縮小
	private void ffmpeg(String fname,Path sourcePath) throws IOException {
		
		String root = Setting.getRoot();
		
		Path p = Paths.get(root,"content","anime-web","upload","img","thumbnail",fname);
		Files.createDirectories(p);
		//String sourcePath=root+"content\\anime-web\\upload\\img\\temp\\"+fname+"."+extension;
		String savePath=p+"\\"+fname+".avif";
		
		//String cmd="echo Y | ffmpeg -i \"{0}\"   -vf scale=480:-1 -compression_level 6 -q:v 18 \"{1}\"" ;
		//cpu
		String cmd="echo Y | ffmpeg -i \"{0}\"    -vf \"scale=if(gt(iw\\,ih)\\,420\\,-2):if(gt(iw\\,ih)\\,-2\\,420)\"  -compression_level 6 -q:v 24  -pix_fmt yuv420p \"{1}\"" ;
		
		String format= MessageFormat.format(cmd,sourcePath.toString(),savePath);	
		/*
		System.out.println(format);
		ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", format);
		 
         // コマンドを実行
         processBuilder.start();
*/
		ExecProcessget.start(format);
		
	}

	
	
}

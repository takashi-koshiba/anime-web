package com.example.web.rest.getFile.anime.image;



import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.Setting;
import com.example.web.rest.getFile.view.image.noImage.NoImage;

@RestController
public class Thumbnail extends FileController {
	@Autowired
	AnimeService animeService;
	@Autowired
	NoImage noImage;
	
	public Thumbnail() {
		super(Setting.getRoot()+"content/anime-web/upload/img/thumbnail/");
	}
	
	@GetMapping("/anime-web/get-file/anime/image/thumbnail/{animeid}")
   public ResponseEntity<Resource> getFile(@PathVariable Integer animeid) {
	    
		
		
		
		try {
			Anime anime = animeService.selectOne(animeid).getFirst();
			ResponseEntity<Resource>  result=super.getFile(anime.getOriginalName()+"/"+anime.getOriginalName()+".avif",anime.getOriginalName()+".avif",false);
			return result;
		}catch(NoSuchElementException e) {
			Log.detail(Level.WARNING, "ファイルの取得に失敗しました。入力ID："+animeid.toString(), e);
			return noImage.getFile();
		}catch(Exception e) {
			Log.detail(Level.WARNING, "未知のエラーが発生しました。入力ID："+animeid.toString(), e);
			return noImage.getFile();
		}
	
		
		//return result;
   }
	protected ResponseEntity.BodyBuilder responseBuilder(){
		ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
				.header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
				.header(HttpHeaders.EXPIRES, ZonedDateTime.now().plusYears(1)
				    .format(DateTimeFormatter.RFC_1123_DATE_TIME));
	//    .header(HttpHeaders.PRAGMA, "no-cache")

	//    .header(HttpHeaders.EXPIRES, String.valueOf(System.currentTimeMillis() + (1000*86400))) ;
    
	//	.header(HttpHeaders.CONTENT_TYPE, contentType);
		return responseBuilder;
	}

	@Override
	protected BodyBuilder responseBuilder(String contentType) {
		ResponseEntity.BodyBuilder responseBuilder =responseBuilder()
				.header(HttpHeaders.CONTENT_TYPE, contentType);

				
		
		return responseBuilder;
	}

	
}

package com.example.web.rest.getFile.anime.video;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.rankedAnime.RankedAnime;
import com.example.web.etc.db.rankedAnime.RankedAnimeService;
import com.example.web.etc.db.video.Video;
import com.example.web.etc.db.video.VideoService;
import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.Setting;

@RestController
public class VideoDL extends FileController {

	
	 private final RankedAnimeService rankedAnimeService;
	    private final VideoService videoService;


	    public VideoDL(RankedAnimeService rankedAnimeService, VideoService videoService) {
	    	
	        super(Setting.getVideoPath()+"\\");

	        this.rankedAnimeService = rankedAnimeService;
	        this.videoService = videoService;
	    }
	
	
	@GetMapping("/anime-web/get-file/anime/video/{anime_id}/{video_id}")
   public ResponseEntity<Resource> getFile(@PathVariable Integer anime_id,@PathVariable Integer video_id,HttpSession session) {
		

		RankedAnime animeInfo =rankedAnimeService.selectOne(anime_id);
		Video videoInfo=videoService.selectOneVideoInfo(video_id);
		

		
		Path p = Paths.get(animeInfo.getFoldername(),videoInfo.getFname()).normalize();
	
		
		return super.getFile(p.toString(),videoInfo.getFname(),true);
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

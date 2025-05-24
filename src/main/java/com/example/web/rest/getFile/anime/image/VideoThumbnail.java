package com.example.web.rest.getFile.anime.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.video.VideoService;
import com.example.web.etc.db.video.VideoThumbnailInfo;
import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.Setting;

@RestController
public class VideoThumbnail extends FileController {
	@Autowired
	VideoService videoService;
	@Autowired
	private ResourceLoader resourceLoader;
	public VideoThumbnail() {
		super(Setting.getRoot()+"content/anime-web/anime/img/");
	}
	
	@GetMapping("/anime-web/get-file/anime/image/video-thumbnail/{videoid}/{filename}")
   public ResponseEntity<Resource> getFile(@PathVariable Integer videoid,@PathVariable String filename) {
		
		VideoThumbnailInfo videoThumbnail= videoService.videoThumbnail(videoid).getFirst();
		Path root=Paths.get( Setting.getRoot()+"content/anime-web/anime/img/");
		Path path=(root.resolve(videoThumbnail.getFoldername()).normalize());
		path=path.resolve(videoThumbnail.getFname()).normalize();
		path=path.resolve(filename+".webp").normalize();

		if(!Files.exists(path)) {
			
		    Resource resource = resourceLoader.getResource("classpath:/static/anime-web/uploader/view/noImage.webp");
		   
		
		    ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
		    		.header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
		    		.header(HttpHeaders.EXPIRES, ZonedDateTime.now().plusYears(1)
		    		    .format(DateTimeFormatter.RFC_1123_DATE_TIME));
		    return responseBuilder.body(resource);
		}
		
		
		return super.getFile(path.toString(),"",false);
   }
	protected ResponseEntity.BodyBuilder responseBuilder(){
		ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
	    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=5184000") ;
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

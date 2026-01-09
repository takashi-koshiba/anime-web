package com.example.web.rest.getFile.view.image.noImage;





import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.uploadFile.UploadFileService;

@RestController
public class NoImage {
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	UploadFileService uploadFileService;

	
	
	//アイテムを表示
	@GetMapping("/anime-web/get-file/view/image/noImage/NoImage")
	public ResponseEntity<Resource> getFile() {
		//System.out.println(session.getAttribute("id"));

			
		    Resource resource = resourceLoader.getResource("classpath:/static/anime-web/uploader/view/noImage.webp");

		
		    ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
		         //   .header(HttpHeaders.CACHE_CONTROL, "public, max-age=604800") 
		         //   .header(HttpHeaders.EXPIRES, String.valueOf(System.currentTimeMillis() + 604800000L)); //7日間
		    		.header(HttpHeaders.CACHE_CONTROL, "private, max-age=60")
		    		.header(HttpHeaders.CONTENT_TYPE, "image/webp")
		    		 .header(
		    		            HttpHeaders.EXPIRES,
		    		            ZonedDateTime.now()
		    		                .plusMinutes(1)
		    		                .format(DateTimeFormatter.RFC_1123_DATE_TIME)
		    		        );
		    		

		    return responseBuilder.body(resource);
		

	    
	}

}

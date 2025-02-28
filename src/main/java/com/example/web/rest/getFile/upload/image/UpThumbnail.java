package com.example.web.rest.getFile.upload.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.Setting;

@RestController
public class UpThumbnail extends FileController {

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	UploadFileService uploadFileService;

	public UpThumbnail() {
		
		super("");
		
	}
	
	
	@GetMapping("/anime-web/get-file/anime/image/upthumbnail/{alias}")
   public ResponseEntity<Resource> getFile(@PathVariable String alias,HttpSession session) {
		if(session.getAttribute("id")==null) {
			return ResponseEntity.badRequest().build();
		}

		List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		if(upfile.size()==0) {
			return ResponseEntity.badRequest().build();
		}

		Path root=Paths.get( "content/anime-web/upload/file/thumbnail/");
		Path path=Paths.get(Setting.getRoot()+root+"/"+upfile.getFirst().getAlias()+".avif").normalize();

		if(!Files.exists(path)) {
			
		    Resource resource = resourceLoader.getResource("classpath:/static/anime-web/uploader/view/noImage.webp");
		   
		
		    ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
		            .header(HttpHeaders.CACHE_CONTROL, "public, max-age=604800") 
		            .header(HttpHeaders.EXPIRES, String.valueOf(System.currentTimeMillis() + 604800000L)); //7日間

		    return responseBuilder.body(resource);
		}
		
		return super.getFile(path.toString(),"",false);
		
   }	
		protected ResponseEntity.BodyBuilder responseBuilder(){
			ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
			    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=10") 
			    .header(HttpHeaders.PRAGMA, "no-cache")

			    .header(HttpHeaders.EXPIRES, String.valueOf(System.currentTimeMillis() + (1000*10))) ;
		    
			//	.header(HttpHeaders.CONTENT_TYPE, contentType);
				return responseBuilder;
			}

			@Override
			protected ResponseEntity.BodyBuilder responseBuilder(String contentType) {
				ResponseEntity.BodyBuilder responseBuilder =responseBuilder()
						.header(HttpHeaders.CONTENT_TYPE, contentType);

						
				
				return responseBuilder;
			}
}

package com.example.web.rest.getFile.view.video;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.Setting;

@RestController
public class VideoFile extends FileController {
	@Autowired
	UploadFileService uploadFileService;
	public VideoFile() {
		super(Setting.getRoot()+"content/anime-web/upload/file/hls/");
	}
	
	@GetMapping("/anime-web/getFile/view/video/hls/{alias}/{width}/video.m3u8")
   public ResponseEntity<Resource> getFile(@PathVariable String alias ,@PathVariable String width ,HttpSession session) {
		
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}

		List<FileInfo> upfile= this.uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		
		if(upfile.size()==0) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ファイルのアクセス権がありません。");
		}
		
		Path path=Paths.get(Setting.getRoot()+"content/anime-web/upload/file/hls/"+alias+"/"+width+"/video.m3u8").normalize();
		if(!Files.isRegularFile(path)) {
			return ResponseEntity.status(HttpStatus.GONE).build();
		}
		
		return super.getFile(path.toString(),"",false);
   }
	@GetMapping("/anime-web/getFile/view/video/hls/{alias}/{width}/{file}.ts")
	   public ResponseEntity<Resource> getFile2(@PathVariable String alias ,@PathVariable String width ,HttpSession session,@PathVariable String file ) {
			
			if(session.getAttribute("id")==null) {
				return ResponseEntity.badRequest().build();
			}

			List<FileInfo> upfile= this.uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
			
			if(upfile.size()==0) {
				return ResponseEntity.badRequest().build();
			}
			
			Path path=Paths.get(Setting.getRoot()+"content/anime-web/upload/file/hls/"+alias+"/"+width+"/"+file+".ts").normalize();
			//System.out.println(path);
			
			return super.getFile(path.toString(),"",false);
	   }
	protected ResponseEntity.BodyBuilder responseBuilder(){
		ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
				.header(HttpHeaders.CACHE_CONTROL, "private, max-age=31536000")
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


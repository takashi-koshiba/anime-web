package com.example.web.rest.getFile.view.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.Setting;
import com.example.web.rest.getFile.view.image.noImage.NoImage;

@RestController
public class SmallThumbnail extends FileController {

	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	NoImage noImage;
	
	@Autowired
	UploadFileService uploadFileService;

	public SmallThumbnail() {
		
		super(Setting.getRoot()+"content/anime-web/upload/file/thumbnail/");
		
	}
	
	
	@GetMapping("/anime-web/get-file/anime/image/small/{alias}")
   public ResponseEntity<Resource> getFile(@PathVariable String alias,HttpSession session) {
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}

		List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		if(upfile.size()==0) {
			Log.log(Level.WARNING, "ファイルのアクセス権がありません。:"+"sessionId:"+session.getAttribute("id")+"/alias:"+alias);
			return noImage.getFile();
			//throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ファイルのアクセス権がありません。");
		}

		
		Path root=Paths.get( "content/anime-web/upload/file/thumbnail/");
		String alias2 = upfile.getFirst().getAlias();
		Path path=Paths.get(Setting.getRoot(),root.toString(),alias2 ,alias2 +".avif").normalize();

		
		
		String fname=upfile.get(0).getFname()+upfile.get(0).getLname();
       
		if(!Files.isRegularFile(path)) {
			return noImage.getFile();
		}
		//return super.getFile(path.toString(), fname, false);
		
		try {
			ResponseEntity<Resource>  result=super.getFile(path.toString(), fname, false);
			return result;
		}catch(ResponseStatusException e) {
			Log.detail(Level.WARNING, "ファイルの取得に失敗しました。："+fname, e);
			return noImage.getFile();
		}catch(Exception e) {
			Log.detail(Level.WARNING, "未知のエラーが発生しました。："+fname, e);
			return noImage.getFile();
		}
		
		
   }
	
	
	protected ResponseEntity.BodyBuilder responseBuilder(){
			ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
					.header(HttpHeaders.CACHE_CONTROL, "private, max-age=31536000")
					.header(HttpHeaders.EXPIRES, ZonedDateTime.now().plusYears(1)
					    .format(DateTimeFormatter.RFC_1123_DATE_TIME));
		    
			//	.header(HttpHeaders.CONTENT_TYPE, contentType);
				return responseBuilder;
			}

			@Override
			protected ResponseEntity.BodyBuilder responseBuilder(String contentType) {
				ResponseEntity.BodyBuilder responseBuilder =responseBuilder()
						.header(HttpHeaders.CONTENT_TYPE, contentType)
				.header(HttpHeaders.CACHE_CONTROL, "private, max-age=31536000")
				.header(HttpHeaders.EXPIRES, ZonedDateTime.now().plusYears(1)
				    .format(DateTimeFormatter.RFC_1123_DATE_TIME));
						
				
				return responseBuilder;
			}
}

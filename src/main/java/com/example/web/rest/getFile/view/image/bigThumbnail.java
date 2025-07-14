package com.example.web.rest.getFile.view.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.Setting;
import com.example.web.rest.getFile.view.file.ItemData;

@Controller
public class  bigThumbnail  extends ItemData {

	


	public bigThumbnail(UploadFileService uploadFileService) {
		
		super(uploadFileService);
		
	}
	
   public ResponseEntity<Resource> getFile(String alias,HttpSession session) {
	   
	   return super.getFile(alias, session,false);
   }
	private boolean isExist(List<FileInfo> upfile,String fpath,String ext) {
		String alias =upfile.getFirst().getAlias();
		
		Path root=Paths.get( Setting.getRoot()+"content/anime-web/upload/file/"+fpath+"/").resolve(alias);
		Path path=(root.resolve(alias+ext).normalize());
		
		Boolean result =Files.exists(path);
		
		if(!result) {
			Log.log(Level.INFO,"パスが見つかりませんでした。"+path);
		}
		
		return result ;

	}
	private Path imagePath(List<FileInfo> upfile,String fpath,String ext) {
		String alias =upfile.getFirst().getAlias();
		Path root=Paths.get(Setting.getRoot()+ "content/anime-web/upload/file/"+fpath+"/").resolve(alias);
		Path path=(root.resolve(alias+ext).normalize());
		return path;

	}
	@Override
	 public Path FilePath(List<FileInfo> upfile) {
		
		 Path path = null;
				 
         if(isExist(upfile,"thumbnail-big",".avif")) {
		     path = imagePath(upfile,"thumbnail-big",".avif");
		     
		 }else if(isExist(upfile,"image","")) {
			 path = imagePath(upfile,"image","");
			
		 }else if(isExist(upfile,"thumbnail",".avif")) {
		     path = imagePath(upfile,"thumbnail",".avif");
		
		 }
         return path;
	}
	protected ResponseEntity.BodyBuilder responseBuilder(){
		ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
				.header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
				.header(HttpHeaders.EXPIRES, ZonedDateTime.now().plusYears(1)
				    .format(DateTimeFormatter.RFC_1123_DATE_TIME));
    
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

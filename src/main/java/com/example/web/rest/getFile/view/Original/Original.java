package com.example.web.rest.getFile.view.Original;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.Setting;
import com.example.web.uploader.sendFile.fileType;

@Controller
public class  Original  extends FileController {
	@Autowired
	UploadFileService uploadFileService;
	


	public Original() {
		
		super("");
		
	}
	
  @GetMapping("/anime-web/get-file/upload/data-original/view/{alias}")
   public ResponseEntity<Resource> getFile(@PathVariable String alias,HttpSession session) {
	  
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}
		
		String  userId=session.getAttribute("id").toString();
		
	//対象ユーザーのファイルを取得
		List<FileInfo> fileInfoList =  uploadFileService.selectFileOne(userId, alias);
		
		System.out.println(alias);
		if( fileInfoList.size()<1) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ファイルが存在しません。");
		}
	  
		FileInfo f=fileInfoList.get(0);
		String filePath=selectPath(f).toString();
		System.out.println(f.getFname()+f.getLname());
	   return super.getFile(filePath ,f.getFname()+f.getLname(), true);
   }
	
	public Path selectPath(FileInfo upfile) {
		String p=Setting.getRoot()+"content/anime-web/upload/file/";
		
		if(upfile.getType()==fileType.AUDIO) {
			Path path=Paths.get(p+"audio/"+upfile.getAlias()).normalize();
			return path;
		}else if(upfile.getType()==fileType.IMAGE) {
			Path path=Paths.get(p+"image/"+upfile.getAlias()).normalize();
			return path;
		}else if(upfile.getType()==fileType.OTHER) {
			Path path=Paths.get(p+"other/"+upfile.getAlias()).normalize();
			return path;
		}else if(upfile.getType()==fileType.VIDEO) {
			Path path=Paths.get(p+"video/"+upfile.getAlias()).normalize();
			return path;
		}
		
		throw new IllegalArgumentException("ファイルタイプが取得できません");
		
		
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

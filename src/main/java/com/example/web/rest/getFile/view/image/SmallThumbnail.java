package com.example.web.rest.getFile.view.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.Setting;

@RestController
public class SmallThumbnail extends FileController {

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	UploadFileService uploadFileService;

	public SmallThumbnail() {
		
		super("");
		
	}
	
	
	@GetMapping("/anime-web/get-file/anime/image/small/{alias}")
   public ResponseEntity<Resource> getFile(@PathVariable String alias,HttpSession session) {
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}

		List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		if(upfile.size()==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ファイルが存在しません。");
		}

		Path root=Paths.get( "content/anime-web/upload/file/thumbnail/");
		Path path=Paths.get(Setting.getRoot()+root+"/"+upfile.getFirst().getAlias()+".avif").normalize();

		if(!Files.exists(path)) {
			
		    Resource resource = resourceLoader.getResource("classpath:/static/anime-web/uploader/view/noImage.webp");
		   
		
		    ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
		         //   .header(HttpHeaders.CACHE_CONTROL, "public, max-age=604800") 
		         //   .header(HttpHeaders.EXPIRES, String.valueOf(System.currentTimeMillis() + 604800000L)); //7日間

		    return responseBuilder.body(resource);
		}
		
		String fname=upfile.get(0).getFname()+upfile.get(0).getLname();
		return super.getFile(path.toString(),fname,false);
   }
}

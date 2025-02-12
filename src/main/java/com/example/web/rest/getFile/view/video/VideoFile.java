package com.example.web.rest.getFile.view.video;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
public class VideoFile extends FileController {
	@Autowired
	UploadFileService uploadFileService;
	public VideoFile() {
		super("");
	}
	
	@GetMapping("/anime-web/getFile/view/video/hls/{alias}/{width}/video.m3u8")
   public ResponseEntity<Resource> getFile(@PathVariable String alias ,@PathVariable String width ,HttpSession session) {
		
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}

		List<FileInfo> upfile= this.uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		
		if(upfile.size()==0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ファイルが存在しません。");
		}
		
		Path path=Paths.get(Setting.getRoot()+"content/anime-web/upload/file/hls/"+alias+"/"+width+"/video.m3u8").normalize();
	
		
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
			System.out.println(path);
			
			return super.getFile(path.toString(),"",false);
	   }
}


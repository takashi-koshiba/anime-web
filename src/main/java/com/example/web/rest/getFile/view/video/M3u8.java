package com.example.web.rest.getFile.view.video;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.Setting;

@RestController
public class M3u8 extends FileController {
	
	@Autowired
	AnimeService animeService;
	private final UploadFileService uploadFileService;
	
	public M3u8(UploadFileService uploadFileService) {
		
		super(Setting.getRoot()+"content/anime-web/upload/file/hls/");
		this.uploadFileService = uploadFileService;
	}
	
	//@GetMapping("/anime-web/get-file/video/m3u8/{alias}")
   public ResponseEntity<Resource> getFile(@PathVariable String alias,HttpSession session) {
		
		if(session.getAttribute("id")==null) {
			return ResponseEntity.badRequest().build();
		}
		
		List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		if(upfile.size()==0 ) {
			return ResponseEntity.badRequest().build();
		}
		
		
	    Path p = Paths.get(alias+"/master.m3u8").normalize();

	    System.out.println(p);
	    
	    return super.getFile(p.toString(),"",false);
   }
}

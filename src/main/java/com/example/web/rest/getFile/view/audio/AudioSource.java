package com.example.web.rest.getFile.view.audio;

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
public class AudioSource extends FileController {
	
	@Autowired
	AnimeService animeService;
	private final UploadFileService uploadFileService;
	
	public AudioSource(UploadFileService uploadFileService) {
		
		super(Setting.getRoot()+"content/anime-web/upload/file/audio/");
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
		
		
	    Path p = Paths.get(alias+".mp3").normalize();
	
	    return super.getFile(p.toString(),"",false);
   }
}

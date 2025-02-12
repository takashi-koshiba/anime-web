package com.example.web.rest.getFile.view.file;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.rest.getFile.view.audio.AudioSource;
import com.example.web.rest.getFile.view.image.bigThumbnail;
import com.example.web.rest.getFile.view.video.M3u8;
import com.example.web.uploader.sendFile.fileType;

@RestController
public class GetData {

	
	@Autowired
	UploadFileService uploadFileService;

	
	
	//アイテムを表示
	@GetMapping("/anime-web/get-file/upload/data/view/{alias}")
   public ResponseEntity<Resource> getFile(@PathVariable String alias,HttpSession session) {
		if(session.getAttribute("id")==null) {
			return ResponseEntity.badRequest().build();
		}
		   
		List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);

		if(upfile.size()==0 ) {
			return ResponseEntity.badRequest().build();
		}
		if(upfile.getFirst().getType()==fileType.IMAGE) {
			bigThumbnail thumbnail=new bigThumbnail(uploadFileService);
			return thumbnail.getFile(alias, session);
			
		}else if(upfile.getFirst().getType()==fileType.VIDEO) {
			M3u8 m3u8=new M3u8(uploadFileService);
			return m3u8.getFile(alias, session);
			
		}else if(upfile.getFirst().getType()==fileType.AUDIO) {
			AudioSource audioSource=new AudioSource(uploadFileService);
			return audioSource.getFile(alias, session);
		}
		
		else {
	
			return ResponseEntity.badRequest().build();
		}
		
		
   }
}

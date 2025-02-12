package com.example.web.rest.getFile.upload.data;

import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.example.web.etc.sta.FileController;
import com.example.web.uploader.sendFile.fileType;

@RestController
public class DataDL extends FileController {

	
	@Autowired
	UploadFileService uploadFileService;

	public DataDL () {
		
		super("");
		
	}
	
	
	@GetMapping("/anime-web/get-file/anime/data/data-dl/{alias}")
   public ResponseEntity<Resource> getFile(@PathVariable String alias,HttpSession session) {
		
		if(session.getAttribute("id")==null) {
			
			return ResponseEntity.badRequest().build();
		}
		
		List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		
		if(upfile.size()==0) {
			return ResponseEntity.badRequest().build();
		}
		fileType  fType = upfile.getFirst().getType();

		Path root=getFilePath(fType);
		Path path=(root.resolve(upfile.getFirst().getAlias()).normalize());

		
		String fname=upfile.getFirst().getFname()+upfile.getFirst().getLname();
		return super.getFile(path.toString(),fname,false);
   }
	
	private Path getFilePath(fileType  fType) {
		Path dir;
		if (fType==fileType.IMAGE){
			dir=Paths.get("content/anime-web/upload/file/image/").normalize();
		}
		else if (fType==fileType.VIDEO){
			dir=Paths.get("content/anime-web/upload/file/video/").normalize();
		}
		else if (fType==fileType.AUDIO){
			dir=Paths.get("content/anime-web/upload/file/audio/").normalize();
		}
		else {
			dir=Paths.get("content/anime-web/upload/file/other/").normalize();
		}
		return dir;
	}
}

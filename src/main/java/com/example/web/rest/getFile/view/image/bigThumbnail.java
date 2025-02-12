package com.example.web.rest.getFile.view.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
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
		Path root=Paths.get( Setting.getRoot()+"content/anime-web/upload/file/"+fpath+"/");
		Path path=(root.resolve(upfile.getFirst().getAlias()+ext).normalize());
	
		return Files.exists(path);

	}
	private Path imagePath(List<FileInfo> upfile,String fpath,String ext) {
		Path root=Paths.get(Setting.getRoot()+ "content/anime-web/upload/file/"+fpath+"/");
		Path path=(root.resolve(upfile.getFirst().getAlias()+ext).normalize());
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
	 
}

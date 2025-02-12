package com.example.web.rest.getFile.view.file;

import java.nio.file.Path;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.FileController;

@Controller
public abstract class  ItemData extends FileController {

	private final UploadFileService uploadFileService;


	public ItemData(UploadFileService uploadFileService) {
		
		super("");
		this.uploadFileService = uploadFileService;
	}
	
   public ResponseEntity<Resource> getFile(String alias,HttpSession session,Boolean canDL) {
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}

		List<FileInfo> upfile= this.uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		
		if(upfile.size()==0) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}

		
		//ファイルが存在すればファイルを取得
		if (FilePath(upfile)!=null) {
			String fname=upfile.get(0).getFname()+upfile.get(0).getLname();
			return super.getFile(FilePath(upfile).toString(),fname,canDL);
		}

		return ResponseEntity.badRequest().build();
		
   }
   

   public abstract Path FilePath(List<FileInfo> upfile) ;

}

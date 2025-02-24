package com.example.web.rest.getFile.view.del;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.Setting;
import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;
import com.example.web.etc.sta.que.upFileDel.DelArgs;
import com.example.web.etc.sta.que.upFileDel.DelFile_Que;

@RestController
public class DelFile  {

	
	@Autowired
	UploadFileService uploadFileService;

	
	//検索にヒットするファイルを取得
	@PostMapping("/anime-web/getFile/view/del/elem")
	public void file(@RequestParam String alias,HttpSession session) {
		if(session.getAttribute("id")==null) {

			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}
		String userId=session.getAttribute("id").toString();
		List<FileInfo> upfile=uploadFileService.selectFileOne(userId,alias);
		if(upfile.size()==0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ファイルが見つかりません。");
		}
		
		String root=Setting.getRoot()+"content\\anime-web\\upload\\";
		
		
		del(root+"file\\audio\\"+alias);
		del(root+"file\\audio\\"+alias+".mp3");
		del(root+"file\\image\\"+alias);
		del(root+"file\\other\\"+alias);
		del(root+"file\\thumbnail\\"+alias+".avif");
		del(root+"file\\thumbnail-big\\"+alias+".avif");
		del(root+"file\\thumbnail-temp\\"+alias+".avif");
		del(root+"file\\video\\"+alias);
		del(root+"file\\hls\\"+alias);
		uploadFileService.delete(userId,alias);
	}
	private void del(String p) {
        //キューに追加
		DelFile_Que delQue = new DelFile_Que();
		Path path=filePath(Paths.get(p).toAbsolutePath().normalize());
        ArgsData delArgs = new DelArgs(path, delQue);
        Que.addToQueue(delArgs);
		
		
		
	}
	private Path filePath(Path p) {
		String root = Setting.getRoot()+"\\content\\anime-web\\upload";
		Path rootPath = Paths.get(root).normalize();
		Path path = rootPath.resolve(p).normalize();
		
		return path;
		
	}
}


package com.example.web.rest.getFile.view.image.seek;

import java.io.File;
import java.io.IOException;
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
import com.example.web.etc.sta.Img;

@RestController
public class SeekImage{

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	UploadFileService uploadFileService;

	public SeekImage() {
		
		
		
	}
	
	
	
	@GetMapping("/anime-web/get-file/anime/image/seek/{alias}/{frame}")
   public ImageInfo getFile(@PathVariable String alias,@PathVariable Integer frame,HttpSession session) {
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}

		List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		if(upfile.size()==0) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ファイルのアクセス権がありません。");
		}
		
		SeekResource seekResource=new SeekResource(resourceLoader);
		ResponseEntity<Resource> seekImage=seekResource.getFile(alias, frame, session);

		Resource resource = seekImage.getBody();
		
		
		if (resource != null) {
			
			try {
				File seekImgFile=resource.getFile();
				Img img=new Img(seekImgFile);
				
				ImageInfo imgInfo=new ImageInfo();
				imgInfo.setHeight(img.getHeight());
				imgInfo.setWidth(img.getWidth());
				
				String p="/anime-web/get-file/anime/image/seek-image/"+alias+"/"+frame;
				imgInfo.setImgPath(p);
				

				return imgInfo;
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ファイルにアクセスできませんでした。");
			} 
			
		}
		
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ファイルの取得に失敗しました。");
		
   }


			
	 public static class ImageInfo {
		private Integer width;
		private Integer height;
		private String imgPath;

		public  Integer getWidth() {
			return this.width;		
		}
		public  Integer getHeight() {
			return this.height;		
		}
		public String getImgPath() {
			return this.imgPath;		
		}
		
		public  void setWidth(Integer w) {
			this.width=w;
		}
		public  void setHeight(Integer h) {
			this.height=h;
		}
		public void setImgPath(String p) {
			this.imgPath=p;
		}
	}
}

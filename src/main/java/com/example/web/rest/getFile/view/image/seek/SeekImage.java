package com.example.web.rest.getFile.view.image.seek;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

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
import com.example.web.etc.sta.Log;
@RestController
public class SeekImage{


	
	@Autowired
	UploadFileService uploadFileService;

	public SeekImage() {
		
		
		
	}
	
	//使用してません
	
	//サムネの大きさとパスを取得
	@GetMapping("/anime-web/get-file/anime/image/seek/{alias}/{frame}")
   public ImageInfo getFile(@PathVariable String alias,@PathVariable Integer frame,HttpSession session) {
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}
		
		String key="alias:" + session.getAttribute("id") + ":" + alias;
		if(session.getAttribute(key)==null) {
			List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
			if(upfile.size()==0) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ファイルのアクセス権がありません。/anime-web/get-file/anime/image/seek/{alias}/{frame}");
			}
			Log.log(Level.INFO, "セッション許可:user"+session.getAttribute("id")+"alias:"+alias);
			
			session.setAttribute(key, "");
		}
		

		


		
		SeekResource seekResource=new SeekResource(uploadFileService);
		ResponseEntity<Resource> seekImage=seekResource.getFile(alias, frame, session);

		
		
		
		if (seekImage != null) {
			
			try {
				File seekImgFile = seekImage.getBody().getFile();
				int width = 0;
				int height = 0;
				try {
				    int[] size = getImageSize(seekImgFile);
				    width = size[0];
				    height = size[1];
				} catch (Exception e) {
				    Log.detail(Level.WARNING, "画像サイズ取得でエラー: /anime-web/get-file/anime/image/seek/{alias}/{frame}", e);
				}

				ImageInfo imgInfo = new ImageInfo();
				imgInfo.setWidth(width);
				imgInfo.setHeight(height);
				imgInfo.setImgPath("/anime-web/get-file/anime/image/seek-image/" + alias + "/" + frame);
				return imgInfo;
				

				
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ファイルにアクセスできませんでした。");
			} 
			
		}else {
			String p="/anime-web/get-file/view/image/noImage/NoImage";
			ImageInfo imgInfo=new ImageInfo();
			//imgInfo.setHeight(img.getHeight());
			//imgInfo.setWidth(img.getWidth());
			imgInfo.setImgPath(p);
			

			return imgInfo;
			
		}
		
		//throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ファイルの取得に失敗しました。");
		
   }


	public static int[] getImageSize(File file) throws Exception {
        try (ImageInputStream in = ImageIO.createImageInputStream(file)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    int width = reader.getWidth(0);
                    int height = reader.getHeight(0);
                    return new int[]{width, height};
                } finally {
                    reader.dispose();
                }
            } else {
            	
                throw new RuntimeException("Unsupported image: " + file.getName());
            }
        }
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

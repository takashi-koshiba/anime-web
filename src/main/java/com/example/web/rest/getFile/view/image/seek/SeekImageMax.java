package com.example.web.rest.getFile.view.image.seek;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
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
public class SeekImageMax  extends FileController {


    private final ResourceLoader resourceLoader;
    private final UploadFileService uploadFileService;

	public SeekImageMax(ResourceLoader resourceLoader, UploadFileService uploadFileService) {
        super("");
        this.resourceLoader = resourceLoader;
        this.uploadFileService = uploadFileService;

		
	}
	
	
	
	//サムネの大きさとパスを取得
	@GetMapping("/anime-web/get-file/anime/image/seekMax/{alias}/{fname}")
   public ResponseEntity<Resource>  getFile(@PathVariable String alias,@PathVariable  String fname,HttpSession session) {
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}
		List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		if(upfile.size()==0) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ファイルのアクセス権がありません。");
		}

		Path maxImagePath = Paths.get(Setting.getRoot(),"content","anime-web","upload","file","maxSeek",alias).resolve(fname).toAbsolutePath().normalize();
	    
		return super.getFile(maxImagePath.toString(), fname, false);

		
		//throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ファイルの取得に失敗しました。");
		
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


		protected ResponseEntity.BodyBuilder responseBuilder(){
				ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
						.header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
						.header(HttpHeaders.EXPIRES, ZonedDateTime.now().plusYears(1)
						    .format(DateTimeFormatter.RFC_1123_DATE_TIME));
				//	.header(HttpHeaders.CONTENT_TYPE, contentType);
					return responseBuilder;
				}

				@Override
				protected ResponseEntity.BodyBuilder responseBuilder(String contentType) {
					ResponseEntity.BodyBuilder responseBuilder =responseBuilder()
							.header(HttpHeaders.CONTENT_TYPE, contentType);

							
					
					return responseBuilder;
				}
}

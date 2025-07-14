package com.example.web.rest.getFile.view.image.seek;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import com.example.web.etc.sta.GetExtension;
import com.example.web.etc.sta.Setting;

@RestController
public class SeekResource extends FileController {


    private final UploadFileService uploadFileService;
	
	
    public SeekResource(UploadFileService uploadFileService) {
        super(Setting.getRoot()+"content/anime-web/upload/file/seek-image/");

        this.uploadFileService = uploadFileService;
    }

	@GetMapping("/anime-web/get-file/anime/image/seek-image/{alias}/{frame}")
   public ResponseEntity<Resource> getFile(@PathVariable String alias,@PathVariable Integer frame,HttpSession session) {
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}
		
		List<FileInfo> upfile= uploadFileService.selectFileOne(session.getAttribute("id").toString(), alias);
		if(upfile.size()==0) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ファイルのアクセス権がありません。");
		}
		
		Path root=Paths.get( "content/anime-web/upload/file/seek-image/");
		Path path=Paths.get(Setting.getRoot(),root.toString(),alias,zeroPad(frame,8)).normalize();
		
		
		

		Path selectedFile = null; 
		if(!Files.exists(path)) {
			Resource resource = new ClassPathResource("static/anime-web/uploader/view/noImage.webp");
			return ResponseEntity.ok()
			    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"noImage.png\"")
			    .body(resource);
		}
		
		List<Path> allFiles = new ArrayList<>();
		try (Stream<Path> paths = Files.list(path)) {
		    allFiles = paths.collect(Collectors.toList());
		} catch (IOException e) {
		    e.printStackTrace();
		}

		Optional<Path> target = allFiles.stream()
		    .filter(p -> p.toString().toLowerCase().endsWith(".webp"))
		    .findFirst();

		if (!target.isPresent()) {
		    target = allFiles.stream()
		        .filter(p -> p.toString().toLowerCase().endsWith(".jpg"))
		        .findFirst();
		}


		//画像がなければ代替画像を使用
		    if (target.isPresent()) {
		        selectedFile = target.get(); 
		    } else {
		        Resource resource = new ClassPathResource("static/anime-web/uploader/view/noImage.webp");
		        return ResponseEntity.ok()
		            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"noImage.png\"")
		            .body(resource);
		    }
		

		
		
		
		
		return super.getFile(selectedFile.toString() ,selectedFile.getFileName().toString(),false);
		
   }

	private static String zeroPad(int number, int length) {
	    return String.format("%0" + length + "d", number);
	}

	private ImageInfo seekImageInfo(File[] files) {
		
        List<File> images = new ArrayList<>();
        int count = 0;

        if (files==null) return null;
        
        for (File image : files) {
            String fname = image.getName();
            if (".jpg".equals(GetExtension.main(fname))) {
                images.add(image);
                count++;
            }
        }

        ImageInfo result = new ImageInfo(images,count);

        return result;
	}
	
	private class ImageInfo{
		private  List<File> images;
		//private  Integer counter;
		
		private ImageInfo(List<File> images,Integer counter) {
			this.images=images;
			//this.counter=counter;
			
		}
		public List<File> getImages() {
			return this.images;
		}
	//	public Integer getCounter() {
	//		return this.counter;
		//}
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

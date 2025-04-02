package com.example.web.rest.getFile.view.image.seek;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

import com.example.web.etc.sta.FileController;
import com.example.web.etc.sta.GetExtension;
import com.example.web.etc.sta.Setting;

@RestController
public class SeekResource extends FileController {

    private final ResourceLoader resourceLoader;
  
    public SeekResource(ResourceLoader resourceLoader) {
        super("");
        this.resourceLoader = resourceLoader;

    }

	@GetMapping("/anime-web/get-file/anime/image/seek-image/{alias}/{frame}")
   public ResponseEntity<Resource> getFile(@PathVariable String alias,@PathVariable Integer frame,HttpSession session) {
		if(session.getAttribute("id")==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}

	

		Path root=Paths.get( "content/anime-web/upload/file/seek-image/");
		Path path=Paths.get(Setting.getRoot()+root+"/"+alias).normalize();
		File[] fileList = path.toFile().listFiles();
		if (fileList == null) {
			 return noImageResource() ;
		    //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ディレクトリが存在しないか、アクセスできません。");
		}
		List<File> images=seekImageInfo(fileList).getImages();
		if( !Files.exists(path)|| images.size()<1) {
			 return noImageResource() ;
			//throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ファイルが存在しません。");
		}
		
		
		Integer fr=frame;
		
		fr=Math.max(1, fr);
		fr=Math.min(fr, images.size()-1);
		
		
		String imagePath=images.get(fr).getAbsolutePath();
		String fname=images.get(fr).toPath().getFileName().toString();
		if(!Files.exists(Paths.get(imagePath))) {
			return  noImageResource() ;
		    
		}
		
		
		return super.getFile(imagePath,fname,false);
		
   }

	public ResponseEntity<Resource>  noImageResource() {
		Resource resource = resourceLoader.getResource("classpath:/static/anime-web/uploader/view/noImage.webp");
	    ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
	         //   .header(HttpHeaders.CACHE_CONTROL, "public, max-age=604800") 
	         //   .header(HttpHeaders.EXPIRES, String.valueOf(System.currentTimeMillis() + 604800000L)); //7日間

	    return responseBuilder.body(resource);
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
			    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=604800") 
			    .header(HttpHeaders.PRAGMA, "cache")

			    .header(HttpHeaders.EXPIRES, String.valueOf(System.currentTimeMillis() + (1000*604800))) ;
		    
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

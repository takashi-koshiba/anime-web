package com.example.web.etc.sta;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;


@Controller
public abstract class FileController {

    private  Path uploadDir;

    // ディレクトリを設定
    public FileController(String dir) {
        String root = Setting.getRoot();
        this.uploadDir = Paths.get(root).resolve(dir).toAbsolutePath().normalize();
        
    }


    public ResponseEntity<Resource> getFile(String filepath,String fname,Boolean canDL) {
    	try {
    		String encodedFname= Paths.get(fname).getFileName().normalize().toString();
    		System.out.println(encodedFname);
    		Path filePath = this.uploadDir.resolve(filepath).normalize();
   
    
    		// セキュリティチェック
    		if (!filePath.startsWith(filePath)) {
    			
    			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
    			
    		}

    		// ファイルのリソース取得
    		Resource resource = new UrlResource(filePath.toUri());
    		if (!resource.exists() || !resource.isReadable()) {

    			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ファイルが存在しません。");
    		}

    		// Content-Typeを取得
 
    		String contentType = Files.probeContentType(filePath);
    		
    		if (contentType == null) {

    			contentType =getContentType(encodedFname);  
    		}
    		
    		//System.out.println(contentType);

    		ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
    		    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=10") 
    		    .header(HttpHeaders.PRAGMA, "no-cache")

    		    .header(HttpHeaders.EXPIRES, String.valueOf(System.currentTimeMillis() + (1000*10))) 
		    
    			.header(HttpHeaders.CONTENT_TYPE, contentType);
    		
    		if (canDL) {
    		    
    		    responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION, 
    		    		"attachment; filename*=UTF-8''" +encodedFname);

    		}

    		return responseBuilder.body(resource);

    				
    	} catch (Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.badRequest().build();
    	}
    }
 // 拡張子に基づいてContent-Typeを設定するメソッド
    private String getContentType(String encodedFname) {
        String fileName = encodedFname.toString().toLowerCase();

        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".bmp")) {
            return "image/bmp";
        } else if (fileName.endsWith(".avif")) {
            return "image/avif";
        }
        else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;  
        }
    }


}


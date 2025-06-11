package com.example.web.etc.sta;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

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
        this.uploadDir = Paths.get(dir).toAbsolutePath().normalize();
        
    }


    public ResponseEntity<Resource> getFile(String filepath,String fname,Boolean canDL) {
    	try {
    		String encodedFname = URLEncoder.encode(fname, StandardCharsets.UTF_8).replace("+", "%20");
    		
    		//System.out.println(encodedFname);
    		Path filePath = this.uploadDir.resolve(filepath).normalize();
   
    		//System.out.println(this.uploadDir);
    		if (!filePath.startsWith(this.uploadDir)) {
    			Log.log(Level.WARNING, "Invalid file path:"+filePath);
    		    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path");
    		}

    		// ファイルのリソース取得
    		Resource resource = new UrlResource(filePath.toUri());
    		if (!resource.exists() || !resource.isReadable()) {
    			Log.log(Level.WARNING, "File not found: " +filePath);
    			throw new ResponseStatusException(HttpStatus.NOT_FOUND, filePath.toString()+"ファイルが存在しません。");
    			
    		}
    		//Log.log(Level.INFO, "File loaaded: " + filePath);

    		// Content-Typeを取得
 
    		//String contentType = Files.probeContentType(filePath);
    		
    		
    		

    		String contentType =getContentType(encodedFname);  
    		
    		
    		//System.out.println(contentType);
    		ResponseEntity.BodyBuilder  responseBuilder=responseBuilder(contentType);
/*
    		ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
    		    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=10") 
    		    .header(HttpHeaders.PRAGMA, "no-cache")

    		    .header(HttpHeaders.EXPIRES, String.valueOf(System.currentTimeMillis() + (1000*10))) 
		    
    			.header(HttpHeaders.CONTENT_TYPE, contentType);
 */   		
    		if (canDL) {
    			String contentDisposition = "attachment; filename*=UTF-8''" + encodedFname;
    		    responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

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
        else if (fileName.endsWith(".webp")) {
            return "image/webp";
        }
        else {
        	
        	
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;  
        }
    }
    protected abstract ResponseEntity.BodyBuilder responseBuilder() ;
    protected abstract ResponseEntity.BodyBuilder responseBuilder(String contentType) ;

}


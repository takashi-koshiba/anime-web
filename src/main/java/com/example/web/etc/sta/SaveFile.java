package com.example.web.etc.sta;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;

public  class SaveFile{


	public static void   main( MultipartFile file,String savePath) {
        try (InputStream inputStream = file.getInputStream();
                OutputStream outputStream = new FileOutputStream(savePath)) {
               byte[] buffer = new byte[8192];
               int bytesRead;
               while ((bytesRead = inputStream.read(buffer)) != -1) {
                   outputStream.write(buffer, 0, bytesRead);
               }
           }
	 catch (IOException e) {
        e.printStackTrace();
        //return "Failed to upload file: " + e.getMessage();
	 }
	
	}
	

}

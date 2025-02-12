package com.example.web.etc.sta;

import jakarta.activation.MimetypesFileTypeMap;



public class MIMEType{


	public  static String main(String filePath) {
		MimetypesFileTypeMap mime = new MimetypesFileTypeMap();
		
        
        return mime.getContentType(filePath);
	}

	

}

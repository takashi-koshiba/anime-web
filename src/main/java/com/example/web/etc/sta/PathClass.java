package com.example.web.etc.sta;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class PathClass {
	public static boolean IsExistFolder(String path) {
		try {
			Path p=Paths.get(path);
			Boolean result = Files.exists(p);
			return result;
			
		}catch(Exception e){
			e.getStackTrace();
			return false;
		}
		
		
		
		
	}
}
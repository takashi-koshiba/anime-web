package com.example.web.etc.sta;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.example.web.rest.settings.directory.Encoders;


public class Setting {
	 private static Properties settings = new Properties();
	 private static FileInputStream fileIn= null;
	// private static FileOutputStream fileOut= null;
	 private static String settingfile="setting.properties";
	 
	public Setting() {
		
	}
	public static void load() throws IOException {
		try {
			fileIn = new FileInputStream(settingfile);
			settings.load(fileIn);
		}catch(IOException IOException) {
			//System.out.println("設定の読み込みに失敗しました。");
		}
		finally{
		
			if(fileIn !=null) {
				fileIn.close();
			}
			
		}

	}
	
	public static  boolean IsExist() {
		return fileIn==null ? false:true;
	}

	public static String  getRoot() {
		
		return settings.getProperty("fullPath")+"\\";
	}
	
	
	
	public static void create() throws IOException {
		FileOutputStream fileOut= null;
		try {
			fileOut = new FileOutputStream(settingfile);
			settings.setProperty("fullPath", System.getProperty("user.dir"));
			settings.store(fileOut, "setting");
		} finally {
			fileOut.close();
		}
	}
	
	public static void setRoot(String path) throws IOException {
		FileOutputStream fileOut= null;
		try {
			fileOut = new FileOutputStream(settingfile);
			Path p= Paths.get(path).toAbsolutePath().normalize();
			settings.setProperty("fullPath", p.toString());
			settings.store(fileOut, "setting");
		} finally {
			fileOut.close();
		}
		

	}
	public static Encoders getEncoder() {
		String enc=settings.getProperty("encoder");
		Encoders[] encoders=Encoders.values();
		Encoders result = Encoders.CPU;
		Integer i=0;
		for(Encoders encoder:encoders) {
			
			if(enc!=null && enc.equals(encoder.toString())) {
				result=encoders[i];
			}
			i++;
		}
		
		return result;
	}
	public static void setEncoder(Encoders enc) throws IOException {
		FileOutputStream fileOut= null;
		try {
			fileOut = new FileOutputStream(settingfile);
			settings.setProperty("encoder", enc.toString());
			settings.store(fileOut, "setting");
		} finally {
			fileOut.close();
		}
	}
	public static String getVideoPath() {
		return settings.getProperty("videoPath");
	}
	
	public static void setVideoPath(String path) throws IOException {
		FileOutputStream fileOut= null;
		try {
			fileOut = new FileOutputStream(settingfile);
			Path p= Paths.get(path).normalize();
			settings.setProperty("videoPath", p.toString());
			settings.store(fileOut, "setting");
		} finally {
			fileOut.close();
		}
	}
	

	

	public static boolean IsUrl(String url) {

		return url.matches("^https?://.+[\\w-]/?$");
	}
	
	public static String getSettingfile() {
		return settingfile;
	}
	public static boolean makeDirectory() {
	    try {
	        String fullPath = Setting.getRoot();
	        
	        Path dir = Paths.get(fullPath,"content", "anime-web", "upload", "img", "thumbnail");
	        Files.createDirectories(dir);
	        dir = Paths.get(fullPath,"content", "anime-web", "upload", "img", "temp");
	        Files.createDirectories(dir);	        
	        dir = Paths.get(fullPath,"content", "anime-web", "upload", "video", "thumbnail");
	        Files.createDirectories(dir);	        
	        dir = Paths.get(fullPath,"content", "anime-web", "anime", "video");
	        Files.createDirectories(dir);        
	        dir = Paths.get(fullPath,"content", "anime-web", "anime", "other", "content");
	        Files.createDirectories(dir);      
	        dir = Paths.get(fullPath,"content", "anime-web", "anime", "other", "log");     
	        Files.createDirectories(dir);
	        dir = Paths.get(fullPath,"content", "anime-web", "anime", "img");     
	        Files.createDirectories(dir);
	        dir = Paths.get(fullPath,"content", "anime-web", "upload", "file","image");     
	        Files.createDirectories(dir);
	        dir = Paths.get(fullPath,"content", "anime-web", "upload", "file","video");     
	        Files.createDirectories(dir);
	        dir = Paths.get(fullPath,"content", "anime-web", "upload", "file","other");     
	        Files.createDirectories(dir);
	        dir = Paths.get(fullPath,"content", "anime-web", "upload", "file","hls");     
	        Files.createDirectories(dir);
	        dir = Paths.get(fullPath,"content", "anime-web", "upload", "file","thumbnail");     
	        Files.createDirectories(dir);
	        dir = Paths.get(fullPath,"content", "anime-web", "upload", "file","thumbnail-big");     
	        Files.createDirectories(dir);
	        dir = Paths.get(fullPath,"content", "anime-web", "upload", "file","thumbnail-temp");     
	        Files.createDirectories(dir);
	        return true;
	        
	    } catch (IOException e) {
	        System.err.println(e.getMessage());
	        return false;

	    } catch (Exception e) {
	        System.err.println(e.getMessage());
	        return false;

	    }
	}
	public static boolean makeAnimeDirectory(String folder) {
		try {
	        String fullPath = Setting.getRoot();
	        folder=folder.trim();

	        
	        Path dir = Paths.get(fullPath, "content","anime-web","anime","video",folder);
	        Files.createDirectories(dir);
	        
	       
	        
	        return true;
	        
	    } catch (IOException e) {
	        System.err.println(e.getMessage());
	        return false;

	    } catch (Exception e) {
	        System.err.println(e.getMessage());
	        return false;

	    }
	}


}

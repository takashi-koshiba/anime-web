package com.example.web;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.web.etc.sta.DeepPathMover;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.PathClass;
import com.example.web.etc.sta.Setting;
import com.example.web.etc.sta.uplaodColumn;
import com.example.web.rest.settings.directory.Encoders;

@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) throws IOException {
        Setting.load();
        
        
        if (!Setting.IsExist()||Setting.getRoot()==null) {
        	//設定がなければ作成
        	System.out.println("設定が見つかりません。");
        	System.out.println("新しく設定を作成します。");
        	
        	Setting.create();
        	Setting.load();
        }	
        
        
        String path=Setting.getRoot();
        System.out.println("ドキュメントルート:"+Paths.get(path).toAbsolutePath());
        if(path==null || !PathClass.IsExistFolder(path)) {
        	String newPath=System.getProperty("user.dir");
        	System.out.println(path+"は存在しないディレクトリです。");
        	System.out.println(newPath+"を作成しました。");
        	
        	Setting.setRoot(newPath);
        	
        }
       

        String videoPath=Setting.getVideoPath();
        if(videoPath==null||!PathClass.IsExistFolder(videoPath)) {
        	String newPath=System.getProperty("user.dir")+"\\content\\anime-web\\anime\\video\\";
        	Setting.setVideoPath(newPath);
        	
        	System.out.println(videoPath+"は存在しないディレクトリです。");
        	System.out.println(newPath+"を登録しました。");
        }
        
        selectEncoder();
        
        Setting.makeDirectory();

        System.out.println(Setting.getRoot()+Setting.getSettingfile()
        +"を読み込みました。");
        
        
        //ファイルアップロードのファイルタイプを設定
        uplaodColumn.setColumnList();
        SpringApplication.run(WebApplication.class, args);
        
        

        //過去のバージョンで作成されたファイルを修正します。
        Path inputPath= Paths.get(Setting.getRoot(), "content", "anime-web", "upload", "file", "image");
        pathFix(inputPath);
        
        inputPath= Paths.get(Setting.getRoot(), "content", "anime-web", "upload", "file", "thumbnail");
        pathFix(inputPath);
        
        inputPath= Paths.get(Setting.getRoot(), "content", "anime-web", "upload", "file", "thumbnail-big");
        pathFix(inputPath);
        
        inputPath= Paths.get(Setting.getRoot(), "content", "anime-web", "upload", "file", "video");
        pathFix(inputPath);
        
        inputPath= Paths.get(Setting.getRoot(), "content", "anime-web", "upload", "file", "other");
        pathFix(inputPath);
        
        inputPath= Paths.get(Setting.getRoot(), "content", "anime-web", "upload", "file", "audio");
        pathFix(inputPath);
        
        inputPath= Paths.get(Setting.getRoot(), "content", "anime-web", "upload", "img", "thumbnail");
        pathFix(inputPath);
        
        
        //seekImageのパスを修正
        Path inputDir=Paths.get(Setting.getRoot(), "content", "anime-web", "upload", "file", "seek-image");
        pathFixDir(inputDir);
        
        Log.log(Level.INFO, "パス修正が完了しました。");
        
    }
    private static void pathFixDir(Path inputDir) {
    	if(!Files.exists(inputDir)) {
    		Log.log(Level.WARNING, "移動対処のパスがありません。:"+inputDir.toString());
    		return;
    	}
    	
    	
    	try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir)) {
    	    for (Path path : stream) {
    	        if (Files.isDirectory(path)) {

    	        	pathFix(path);
    	        }
    	    }
    	}catch (IOException e) {
    		Log.detail(Level.WARNING, "パスの修正で異常な問題が発生:",e);
		}

    	
    }
    
    private static void pathFix(Path inputPath) {
    	if(!Files.exists(inputPath)) {
    		Log.log(Level.WARNING, "移動対処のパスがありません。:"+inputPath.toString());
    		return;
    	}
    	
    	Log.log(Level.INFO, "パスの修正開始："+inputPath);
    	
    	try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputPath)) {
    		
    		
    	    for (Path path : stream) {
    	        if (Files.isRegularFile(path)) {
    	        	DeepPathMover.move(path);
    	        }
    	    }
    	} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			Log.detail(Level.WARNING, "パスの修正で異常な問題が発生:",e);
		}
    	
    	

    	Log.log(Level.INFO, "imageパスの修正が完了しました。");

    }
    private static void selectEncoder() throws IOException {
        Encoders encoder=Setting.getEncoder();
       
        if(encoder==null) {
        	Setting.setEncoder(Encoders.CPU);
        }
        
        System.out.println("エンコーダーは"+encoder.toString()+"を使用します。");
    }
}

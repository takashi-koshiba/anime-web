package com.example.web;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.web.etc.sta.PathClass;
import com.example.web.etc.sta.Setting;
import com.example.web.etc.sta.uplaodColumn;

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
        
        Setting.makeDirectory();

        System.out.println(Setting.getRoot()+Setting.getSettingfile()
        +"を読み込みました。");
        
        
        //ファイルアップロードのファイルタイプを設定
        uplaodColumn.setColumnList();
        SpringApplication.run(WebApplication.class, args);
    }
}

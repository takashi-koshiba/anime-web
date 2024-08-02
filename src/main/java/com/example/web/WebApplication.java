package com.example.web;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.web.etc.sta.PathClass;
import com.example.web.etc.sta.Setting;

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
        if(path==null || !PathClass.IsExistFolder(path)) {
        	String newPath=System.getProperty("user.dir");
        	System.out.println(path+"は存在しないディレクトリです。");
        	System.out.println(newPath+"を作成しました。");
        	
        	Setting.setRoot(newPath);
        	
        }
        String url=Setting.getUrl();
        
        if(url==null||Setting.IsUrl(url)) {
        	String newUrl="http://localhost/";
        	Setting.setUrl(newUrl);
        }
        
        Setting.makeDirectory();

        System.out.println(Setting.getRoot()+Setting.getSettingfile()
        +"を読み込みました。");
        

        SpringApplication.run(WebApplication.class, args);
    }
}

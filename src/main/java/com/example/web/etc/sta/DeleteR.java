package com.example.web.etc.sta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


//パスの末尾に　/をつけると親フォルダは削除されません。
public class DeleteR {
	public static void main(Path p,Boolean delParentDir) {
		Path path= p.toAbsolutePath().normalize();
		File dir = path.toFile();
		File[] lists = dir.listFiles();//ディレクトリとファイルを取得
	    if(lists ==null) return ;
	    
	    for(File list :lists) {
	    	if(list .isFile()) {
	            //list.toString //削除
	    		delFile(list.toPath());
	        }
	        //ディレクトリの場合
	        else if(list.isDirectory()) {
	            DeleteR.main(list.toPath(),delParentDir);
	            delDir(list.toPath());
	        }
	    }
	    if(delParentDir) {
	    	 delDir(path); // すべての子を削除した後にフォルダを削除
	    }
	   
		 
	      //  System.out.println(list.length);
	}
	private static void delFile(Path f) {
		try {
			Files.deleteIfExists(f);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ファイルの削除に失敗しました。");
		}
		
	}
	private static void delDir(Path f) {
		try {
			Files.deleteIfExists(f);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "フォルダの削除に失敗しました。");
		}
	}
}

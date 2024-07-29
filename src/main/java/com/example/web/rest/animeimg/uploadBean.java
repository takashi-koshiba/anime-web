package com.example.web.rest.animeimg;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class uploadBean{
	private MultipartFile file;
	private String folder;
	private String foldername;
	private String originalName;
	

	public uploadBean(MultipartFile file,String folder,String foldername,String originalName) {
		this.file=file;//画像ファイル
		this.folder=folder;//アップロード先
		this.foldername=foldername;//フォルダ名
		this.originalName=originalName;//タイトル
	}
	
}

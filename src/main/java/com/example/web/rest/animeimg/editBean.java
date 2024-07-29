package com.example.web.rest.animeimg;

import lombok.Data;


@Data
public class editBean{
	private String  fname;
	private String title;
	private String foldername;
	private boolean IsExist;
	

	public editBean(String fname,String folderName,String title,boolean IsExist) {
		this.fname=fname;//画像のファイル名
		this.foldername=folderName;//フォルダ名
		this.title=title;//タイトル
		this.IsExist=IsExist;
	}
	
}

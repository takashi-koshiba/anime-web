package com.example.web.rest.animeimg.Animetable;

import lombok.Data;

@Data
public class Anime {
	//private int id;
	private String originalName;
	private String foldername;
	
	
	public  Anime(String originalName,String foldername) {
		this.originalName=originalName;
		this.foldername=foldername;
	}
}
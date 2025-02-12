package com.example.web.anime.video;

import lombok.Data;

@Data
public class ObjSort {
	String title;
	Integer index;
	
	public ObjSort(String title,Integer index) {
		this.title=title;
		this.index=index;
	}
}

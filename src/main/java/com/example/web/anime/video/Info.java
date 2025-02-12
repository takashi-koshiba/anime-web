package com.example.web.anime.video;

import lombok.Data;

@Data
public class Info {
	private Integer sortId;
	private Integer index;
	private Integer nextIndex;
	private String sortName;
	private ObjSort[]  arr;
	//private ObjSort[]  arr=new ObjSort[] {new ObjSort("タイトル",1),new ObjSort("日付",4)};
	
	
	public Info(Integer index,ObjSort[] arr) {
		this.arr=arr;
		this.nextIndex=getNextIndex(index);
		this.index=index;
		this.sortId=this.arr[this.nextIndex].getIndex();
		this.sortName=this.arr[this.nextIndex].getTitle();
	}
	
	public Integer getNextIndex(Integer index) {
		
		Integer len=arr.length;
		if(index<len-1) {
			return index+1;
			
		}
		return 0;
		
	}
	public String getTitle(Integer i) {
		
		return  this.arr[i].getTitle();
		
		
	}
}

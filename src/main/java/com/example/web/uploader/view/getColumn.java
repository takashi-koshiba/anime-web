package com.example.web.uploader.view;

import java.util.ArrayList;
import java.util.List;

import com.example.web.etc.sta.Columns;


public class getColumn {

	private List<Columns> columns;
	
	
	public getColumn() {
		this.columns=new ArrayList<Columns>();
		
		
	}
	public void addValue(Integer orderId,String columnName) {
		Columns column=new Columns(orderId,columnName);
		this.columns.add(column);
	}
	
	public List<Columns> getColumns(){
		return this.columns;
		
	}
	
}

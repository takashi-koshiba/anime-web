package com.example.web.uploader.view;

import lombok.Data;

@Data
public class CalcDistanceFile {

	Integer fileId;
	String fname;
	String lname;
	Double rate;
	String alias;
	Double matchRate;
	
	public CalcDistanceFile(Integer id,String fname,String lname,String alias,Double matchRate) {
		this.fileId=id;
		this.fname=fname;
		this.lname=lname;
		this.alias=alias;
		this.matchRate=matchRate;
	}
}

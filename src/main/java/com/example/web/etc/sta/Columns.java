package com.example.web.etc.sta;

import lombok.Data;

@Data
public class Columns {

	private Integer orderId;
	private String columnName;
	
	public Columns(Integer orderId,String columnName) {
		this.orderId=orderId;//列の順番
		this.columnName=columnName;//列名
		
	}
	
	
}

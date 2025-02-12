package com.example.web.uploader.view;

import lombok.Data;

@Data
public class Type {

	int id;
	String value;
	public Type(int id,String value) {
		this.id=id;
		this.value=value;
	}
}

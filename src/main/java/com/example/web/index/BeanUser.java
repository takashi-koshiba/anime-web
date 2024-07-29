package com.example.web.index;

import lombok.Data;

@Data
public class BeanUser {

	private String ip;
	private String name;
	private boolean root;
	public BeanUser(String ip, String name,boolean root) {
		this.ip=ip;
		this.name=name;
		this.root=root;
	}
	
}

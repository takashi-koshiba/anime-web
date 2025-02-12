package com.example.web.etc.sta;

public class GetExtension {
	public static String main(String path) {
		Integer index = path.lastIndexOf(".");
		
		if(index<0) {
			return null;
		}
		return path.substring(index);
	 }
}

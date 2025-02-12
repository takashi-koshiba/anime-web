package com.example.web.etc.sta;

import java.text.MessageFormat;

public class Kakasi {
	public static String main(String string,String opt) {
		
	String text= string;
	String format= MessageFormat.format("echo \"{0}\" | kakasi "+opt,text);		

	String result=ExecProcessget.start(format);
	System.out.print(result.substring(1,result.length()-3));
	return result.substring(1,result.length()-3);
	 }
}

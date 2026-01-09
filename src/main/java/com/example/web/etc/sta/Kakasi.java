package com.example.web.etc.sta;

import java.text.MessageFormat;
import java.text.Normalizer;

public class Kakasi {
	public static String main(String string,String opt) {
		
	String text= string;
	String format= MessageFormat.format("echo \"{0}\" | kakasi "+opt,text);		

	String result=ExecProcessget.start(format);
	
	return result.substring(1,result.length()-3);
	 }
	public static String katakanaToHiragana(String s) {
		 String normalized = Normalizer.normalize(s, Normalizer.Form.NFKC);
	    char[] chars = normalized.toCharArray();
	    for (int i = 0; i < chars.length; i++) {
	        char c = chars[i];
	        // カタカナ Unicode 範囲
	        if (c >= 'ァ' && c <= 'ン') {
	            chars[i] = (char)(c - 0x60);
	        }
	    }
	    return new String(chars);
	}
}


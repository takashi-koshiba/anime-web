package com.example.web.etc.sta;

public class TextRep {
	public static String main(String text2) {
		String text=text2;
		text=text.replace("ゐ", "い");
		text=text.replace("ゑ", "え");
		text=text.replace("ゎ", "わ");
		text=text.replace("ヰ", "イ");
		text=text.replace("ヱ", "エ");
		
		return text;
	}
}

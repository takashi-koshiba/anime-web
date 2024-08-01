package com.example.web.etc.sta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.MessageFormat;

public class Kakasi {
	public static String main(String string) {
	String text= string;
	text=TextRep.main(text);
	String format= MessageFormat.format("echo {0} | kakasi -JH -KH",text);		

			
	 try {
         // Windowsでcmd.exeを使用してパイプを含むコマンドを実行
         ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", format);

         // コマンドを実行
         Process process = processBuilder.start();

         // 結果を読み取る（UTF-8エンコーディングを指定）
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("Shift_JIS")));
  
         //while ((line = reader.readLine()) != null) {
             return reader.readLine();
         //}

     } catch (IOException e) {
         e.printStackTrace();
     }
	 return null;
	 }
}

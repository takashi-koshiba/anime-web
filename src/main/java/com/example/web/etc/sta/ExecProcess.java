package com.example.web.etc.sta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ExecProcess {
	public static BufferedReader main(String cmd) {

		try {
	         // Windowsでcmd.exeを使用してパイプを含むコマンドを実行
	         ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", cmd);
	         
	         // コマンドを実行
	         java.lang.Process process = processBuilder.start();
	         // 標準出力とエラー出力を非同期で読み取る
	            Thread outputReader = new Thread(() -> readStream(process.getInputStream(), "OUTPUT"));
	            Thread errorReader = new Thread(() -> readStream(process.getErrorStream(), "LOG"));

	            outputReader.start();
	            errorReader.start();

	            // プロセスの終了を待機
	          //  int exitCode = process.waitFor();

	            // スレッドの終了を待機
	            outputReader.join();
	            errorReader.join();
	         System.out.println(cmd);

	         
	         // 結果を読み取る（UTF-8エンコーディングを指定）
	         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("Shift_JIS")));


	        
	        
	         return reader;
	         //}

	     } catch (IOException | InterruptedException e) {
	         e.printStackTrace();
	     } finally {

	    	 
		}
		return null;
	} // ストリームを読み取るメソッド
    private static void readStream(java.io.InputStream inputStream, String streamType) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[" + streamType + "] " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.example.web.etc.sta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ExecProcessget {
    public static String start(String cmd) {
        try {
            // Windowsでcmd.exeを使用してパイプを含むコマンドを実行
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", cmd);
            Process process = processBuilder.start();

            // 標準出力とエラー出力を格納するバッファ
            StringBuilder outputLog = new StringBuilder();
            StringBuilder errorLog = new StringBuilder();

            // スレッドでストリームを読み取る
            Thread outputReader = new Thread(() -> readStream(process.getInputStream(), "OUTPUT", outputLog, Charset.forName("Shift_JIS")));
            Thread errorReader = new Thread(() -> readStream(process.getErrorStream(), "LOG", errorLog, Charset.forName("Shift_JIS")));

            outputReader.start();
            errorReader.start();

            // プロセスの終了を待機
            int exitCode = process.waitFor();

            // スレッドの終了を待機
            outputReader.join();
            errorReader.join();
            System.out.println("=== cmd ===");
            System.out.println(cmd);
            System.out.println("=== OUTPUT ===");
            System.out.println(outputLog.toString());

            System.out.println("=== LOG ===");
            System.out.println(errorLog.toString());

            System.out.println("Process exited with code: " + exitCode);

            // 標準出力の内容を返す
            return outputLog.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void readStream(InputStream inputStream, String streamType, StringBuilder logStorage, Charset charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logStorage.append(line).append("\n"); 
                System.out.println(streamType + ": " + line); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

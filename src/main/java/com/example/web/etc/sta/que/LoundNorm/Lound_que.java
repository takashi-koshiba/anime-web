package com.example.web.etc.sta.que.LoundNorm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Pattern;

import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;

public class Lound_que extends Que  {
	
	public  Lound_que() {
		super();
	}
	
	static void encode(String in, String out, Map<String, String> m) throws Exception {
		
		
	    String filter = String.format(
	        "loudnorm=I=-16:TP=-1.5:LRA=11:measured_I=%s:measured_TP=%s:measured_LRA=%s:measured_thresh=%s:offset=%s:linear=true:print_format=summary",
	        m.get("measured_I"),
	        m.get("measured_TP"),
	        m.get("measured_LRA"),
	        m.get("measured_thresh"),
	        m.get("offset")
	    );

	    // コマンドを引数単位で渡す（空白や日本語パスも安全）
	    List<String> cmd = Arrays.asList(
	        "ffmpeg", "-y", "-nostdin",
	        "-i", in,
	        "-af", filter,
	        "-vn",              
	        "-threads", "4",
	        "-c:a", "flac",     
	        out
	    );

	    // ログ出力
	    Log.log(Level.INFO, "Executing encode command: " + String.join(" ", cmd));

	    // 実行
	    ProcessBuilder pb = new ProcessBuilder(cmd).redirectErrorStream(true);
	    Process process = pb.start();

	    // 出力読み取り（念のためログへ）
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            Log.log(Level.INFO, "ENCODE: " + line);
	        }
	    }

	    int exitCode = process.waitFor();
	    if (exitCode != 0) {
	        throw new RuntimeException("ffmpeg encode failed, exit=" + exitCode);
	    }
	}

    
	@Override
	protected void process(ArgsData argsdata) {
		String input =argsdata.getArgument("input").toString();
		String output =argsdata.getArgument("output").toString();
		// ExecProcess.main(argsdata.getArgument("cmd").toString());
		List<String> firstPass = Arrays.asList(
			    "ffmpeg","-y","-nostdin",
			    "-i", input,
			    "-af","loudnorm=I=-16:TP=-1.5:LRA=11:print_format=json",
			    "-f","null","-"
			);

		try {
			System.out.println("jikkoumaemae");
			String j=jsonData(firstPass);
			System.out.println("jikkouk");
			Log.log(Level.INFO, j);
			
			System.out.println("jikkoumae");
			Map<String,String> audioParam= getValuesFromJson(j);
			if (audioParam.values().stream().anyMatch(Objects::isNull)) {
			    Log.log(Level.WARNING, "loudnorm JSON が解析できません");  
			    return;                        // 処理を打ち切る
			}
			
			Log.log(Level.INFO, audioParam.toString());
			
			Files.createDirectories(Paths.get(output).getParent());
			encode(input, output, audioParam);
			
		}catch (Exception e) {
		    Log.detail(Level.WARNING, "ラウドネス正規化失敗", e);
		    return;        
		}
		
		
	}
	
	private String jsonData(List<String> cmd) throws IOException, InterruptedException {
	    Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();

	    StringBuilder log = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(
	            new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            log.append(line).append('\n');
	        }
	    }
	    int exit = p.waitFor();
	    if (exit != 0) throw new RuntimeException("ffmpeg failed, exit=" + exit);
	    return log.toString();
	}



	
	private Map<String, String> getValuesFromJson(String json) {
	    Pattern pt = Pattern.compile("\"(input_i|input_tp|input_lra|input_thresh|target_offset)\"\\s*:\\s*\"?([\\-0-9\\.]+)\"?");
	    var m = pt.matcher(json);
	    Map<String,String> map = new HashMap<>();
	    while (m.find()) map.put(m.group(1), m.group(2));

	    // 入力→measured_ の変換
	    Map<String,String> measured = new HashMap<>();
	    measured.put("measured_I",       map.get("input_i"));
	    measured.put("measured_TP",      map.get("input_tp"));
	    measured.put("measured_LRA",     map.get("input_lra"));
	    measured.put("measured_thresh",  map.get("input_thresh"));
	    measured.put("offset",           map.get("target_offset"));
	    return measured;
	}

	private static void readStream(InputStream inputStream, String streamType, StringBuilder logStorage, Charset charset) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logStorage.append(line).append("\n"); 
                System.out.println(streamType + ": " + line);
                Log.log(Level.INFO, streamType + ": " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.detail(Level.WARNING, "不明なエラー", e);
        }
    }
}

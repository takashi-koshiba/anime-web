package com.example.web.etc.sta;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.web.etc.sta.que.ArgsData;
import com.example.web.rest.settings.directory.Encoders;





public class Hls {
	
	
    static final BlockingQueue<ArgsData> encodingQueue = new LinkedBlockingQueue<>();
    static boolean isEncoding = false;

    
	public static String main(ArgsData args) {

		//String c ="ffmpeg -i \"{0}\" -c:v copy -c:a copy -f hls -hls_time 5 -force_key_frames expr:gte(t,n_forced*5) -hls_playlist_type vod -hls_segment_filename \"{1}video%3d.ts\" {1}video.m3u8";
		String input=args.getArgument("input").toString();
		Path output = Paths.get(args.getArgument("output").toString());
		String url=args.getArgument("url").toString();
		
		int [] ScreenSizeArr=getScreenSize(input);
		int width=ScreenSizeArr[0];
		int height=ScreenSizeArr[1];
		
		
		

		try {
			FileWriter file = new FileWriter(output+"\\master.m3u8");
			PrintWriter m3u8 = new PrintWriter(new BufferedWriter(file));
			 m3u8.write("#EXTM3U\r\n");
			 m3u8.write("#EXT-X-VERSION:3\r\n");
			 m3u8.close();
		} catch (IOException e) {
            e.printStackTrace();
        }

			if(height>=180) {
				int resizeH=180;
				double ratio = (double)resizeH/(double)height;
				int resizeW=(int)(width*ratio);
				resizeW=resizeW%2==0?resizeW:resizeW+1;
				int bandwidth=200000;
				String f=createFolderAndEncode(output,input,resizeH,resizeW,bandwidth);
				AddM3u8(output,(int)bandwidth*5,f,url+"/"+resizeH);
			} 
			if(height>=360) {
				int resizeH=360;
				double ratio = (double)resizeH/(double)height;
				int resizeW=(int)(width*ratio);
				resizeW=resizeW%2==0?resizeW:resizeW+1;
				int bandwidth=300000;
				String f=createFolderAndEncode(output,input,resizeH,resizeW,bandwidth);
				AddM3u8(output,(int)bandwidth*5,f,url+"/"+resizeH);
			}
			if(height>=720) {
				int resizeH=720;
				double ratio = (double)resizeH/(double)height;
				int resizeW=(int)(width*ratio);
				resizeW=resizeW%2==0?resizeW:resizeW+1;
				int bandwidth=1000000;

				String f=createFolderAndEncode(output,input,resizeH,resizeW,bandwidth);
				AddM3u8(output,(int)bandwidth*5,f,url+"/"+resizeH);
			}
			if(height>=1080) {
				int resizeH=1080;
				double ratio = (double)resizeH/(double)height;
				int resizeW=(int)(width*ratio);
				resizeW=resizeW%2==0?resizeW:resizeW+1;
				int bandwidth=4000000;
				String f=createFolderAndEncode(output,input,resizeH,resizeW,bandwidth);
				AddM3u8(output,(int)bandwidth*5,f,url+"/"+resizeH);
			}
			int bandwidth= getBitRate(input)*5;
			//System.out.println( getBitRate(input));
			String f=createFolderAndEncode(output,input,-1,-1,bandwidth);
			AddM3u8(output,(int)bandwidth*5,f,url+"/-1");
			
		return null;
	}
	
	private static void AddM3u8(Path outputDir,Integer bandwidth,String dir,String url) {
		FileWriter filewriter;
		try {
			filewriter = new FileWriter(outputDir+"\\master.m3u8",true);
			PrintWriter m3u8 = new PrintWriter(new BufferedWriter(filewriter));
			
			String str="#EXT-X-STREAM-INF:BANDWIDTH={0,number,#}";
			String format=MessageFormat.format(str,bandwidth);	
			

			m3u8.write(format+"\r\n");
			m3u8.write(url+"/video.m3u8\r\n");
			m3u8.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		
	}
	private static int getBitRate(String inputPath) {
	
		
		String c ="ffprobe -v error -of flat=s=_ -select_streams v:0 -show_entries format=bit_rate \"{0}\" ";
		String format= MessageFormat.format(c,inputPath);	
		String str=ExecProcessget.start(format);
		String regex = "[0-9]+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
		    return Integer.parseInt(matcher.group());
		} else {
		    return 0;
		}
		/*BufferedReader r=ExecProcess.main(format);
		String line;
		try {
			line = r.readLine();
			String regex = "[0-9]+";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
			    return Integer.parseInt(matcher.group());
			} else {
			    return 0;
			}
			
			
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return 0;
		}
*/
	}
	private static String createFolderAndEncode(Path outputDir, String inputPath,int h,int w ,int bitRate) {
		 try {
	        	Path dir = Paths.get(outputDir.toString(),String.valueOf(h));     
				Files.createDirectories(dir);
				String encodePath= MessageFormat.format(outputDir.toString()+"\\{0,number,#}\\",h);	
				processEncode(inputPath,encodePath,bitRate,h,w);

				return encodePath;
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return null;
			}

			
	}
	private static void processEncode(String inputPath,String outputDir,int bitRate,int height,int width) {
		String format;
		String c;
		if(height==-1) {
			switch (Setting.getEncoder()) {
				case Encoders.CPU: {
					c ="ffmpeg -i \"{0}\"   -c:v libx264 -preset ultrafast -crf 28  "
							+ "   -filter:a loudnorm=I=-10:LRA=11:TP=-1.5 "
							+ "-f hls -hls_time 2 -force_key_frames expr:gte(t,n_forced*5) "
							+ "-hls_playlist_type vod -hls_segment_filename \"{1}video%3d.ts\" "
							+ " \"{1}video.m3u8\"";
					/*
					
						*/	
					
					break;
				}
				case Encoders.NVENC:{
					/*
					c = "ffmpeg -i \"{0}\" "
						      + "-c:v h264_nvenc -cq 28"
						      + "-filter:a loudnorm=I=-10:LRA=11:TP=-1.5 "
						      + "-f hls -hls_time 2 -force_key_frames expr:gte(t,n_forced*5) "
						      + "-hls_playlist_type vod -hls_segment_filename \"{1}video%03d.ts\" "
						      + " \"{1}video.m3u8\"";
						     */
					c="NVEncC64.exe "
							+ "-i  \"{0}\"  -o \"{1}video.m3u8\" -f hls "
							+ "-m hls_segment_filename:\"{1}video%3d.ts\" -m hls_list_size:0  "
							+ "-c h264 --audio-codec aac   --avcuvid --input-analyze 30 "
							+ "--lookahead 32 --aq --aq-temporal --aq-strength 0 -m hls_time:2 "
							+ "--vbrhq 0 --vbr-quality 28 --gop-len 120 --bframes 2  "
							+ "--cqp 21:23:25   --bref-mode each  ";
					break;
				}
				
				
				default:
					throw new IllegalArgumentException("Unexpected value: " + Setting.getEncoder());
				
				
			}
			format= MessageFormat.format(c,inputPath,outputDir);
				
		}else {
			switch (Setting.getEncoder()) {
			case Encoders.CPU: {
				
				c = "ffmpeg -i \"{0}\"  -c:v libx264 -preset ultrafast -crf 30 "
						+ "-vf \"scale={4,number,#}:{3,number,#}\" -b:v {2,number,#} "
						+"-filter:a loudnorm=I=-10:LRA=11:TP=-1.5 -f hls -hls_time 2 "
						+"-force_key_frames expr:gte(t,n_forced*5) "
						+ "-hls_playlist_type vod -hls_segment_filename \"{1}video%03d.ts\" "
						+ " \"{1}video.m3u8\"";
				break;
			}
			case Encoders.NVENC:{
				/*
				c = "ffmpeg -i \"{0}\" "
					      + "-c:v h264_nvenc -preset p1 -cq 28 "
					      + "-vf \"scale={4,number,#}:{3,number,#}\" -b:v {2,number,#} "
					      + "-filter:a loudnorm=I=-10:LRA=11:TP=-1.5 "
					      + "-f hls -hls_time 2 -force_key_frames expr:gte(t,n_forced*5) "
					      + "-hls_playlist_type vod -hls_segment_filename \"{1}video%03d.ts\" "
					      + " \"{1}video.m3u8\"";
*/	
				bitRate=bitRate/1000;
				c="NVEncC64.exe "
						+ "-i  \"{0}\"  -o \"{1}video.m3u8\" -f hls "
						+ "-m hls_segment_filename:\"{1}video%3d.ts\" -m hls_list_size:0  "
						+ "-c h264 --audio-codec aac   --avcuvid --input-analyze 30 "
						+ "--lookahead 96 --aq --aq-temporal --aq-strength 1 "
						+ "--vbrhq 1 --vbr-quality 20 --gop-len 120 --bframes 5  "
						+ "--cqp 40:45:50   --bref-mode each -m hls_time:2 "
						+ "--output-res {4,number,#}x{3,number,#} "
						+ "  --max-bitrate {2,number,#} --preset p1  ";
				if(height>=720) {
					c="NVEncC64.exe "
							+ "-i  \"{0}\"  -o \"{1}video.m3u8\" -f hls "
							+ "-m hls_segment_filename:\"{1}video%3d.ts\" -m hls_list_size:0  "
							+ "-c h264 --audio-codec aac   --avcuvid --input-analyze 30 "
							+ "--lookahead 32 --aq --aq-temporal --aq-strength 1 "
							+ "--vbrhq 1 --vbr-quality 28 --gop-len 96 --bframes 5  "
							+ "--cqp 24:28:32   --bref-mode each -m hls_time:2 "
							+ "--output-res {4,number,#}x{3,number,#} "
							+ "  --max-bitrate {2,number,#} --preset p4  ";
				}
				
				break;
			}
			
			default:
				throw new IllegalArgumentException("Unexpected value: " + Setting.getEncoder());
			}

			
			format = MessageFormat.format(c, inputPath, outputDir, bitRate, height,width);
			

			
			
		}

		//ExecProcess.main(format);
		ExecProcessget.start(format);
		
		
		
	}
	
	private static int[] getScreenSize(String inputPath){
		String c= "ffprobe -v error -of flat=s=_ -select_streams v:0 -show_entries stream=height,width \"{0}\"";
		String format= MessageFormat.format(c,inputPath);	
		String str=ExecProcessget.start(format);
		int width,height;
		width=height=0;
		if (str.contains("streams_stream_0_width=")) {
		    String match = pattern(str, "streams_stream_0_width=(\\d+)");
		    width = Integer.parseInt(match); 
		   
		}
		if (str.contains("streams_stream_0_height=")) {
		    String match = pattern(str, "streams_stream_0_height=(\\d+)");
		    height = Integer.parseInt(match); 
		    
		}

	     System.out.println("width:"+width);
	     System.out.println("height:"+height);
	     if(width!=0 && height!=0) {
	    	 int result[] = {width,height};
	    	  
			 return result; 
	     }
	     
	     int[] result = {0,0};
	     return result;
	}
	private static String pattern(String str, String pattern) {
	    System.out.println("Input String: " + str);
	    System.out.println("Regex Pattern: " + pattern);

	    // \r を削除して、改行を無視する
	    str = str.replaceAll("\r", "");
	    
	    // 正規表現を使ってマッチング
	    Pattern p = Pattern.compile(pattern, Pattern.DOTALL); // 改行を含めてマッチ
	    Matcher m = p.matcher(str);

	    if (m.find()) { 
	        return m.group(1); // キャプチャグループ 1 を取得
	    }

	    return null;  // マッチしなかった場合
	}


}

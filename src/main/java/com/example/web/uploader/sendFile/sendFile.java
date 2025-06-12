package com.example.web.uploader.sendFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Objects;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.extensionUpload.extensionUpload;
import com.example.web.etc.db.extensionUpload.extensionUploadService;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.db.upload_hash.Upload_hashService;
import com.example.web.etc.sta.DeleteR;
import com.example.web.etc.sta.GetExtension;
import com.example.web.etc.sta.Img;
import com.example.web.etc.sta.RemoveExtension;
import com.example.web.etc.sta.SaveFile;
import com.example.web.etc.sta.Setting;
import com.example.web.etc.sta.ToHash256;
import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;
import com.example.web.etc.sta.que.SecondQue;
import com.example.web.etc.sta.que.cmd.Cmd_Args;
import com.example.web.etc.sta.que.cmd.Cmd_que;
import com.example.web.etc.sta.que.createMaxThumbnail.Thumbnail_Args;
import com.example.web.etc.sta.que.createMaxThumbnail.Thumbnail_que;
import com.example.web.etc.sta.que.hls.HlsArgs;
import com.example.web.etc.sta.que.hls.Hls_Que;
import com.example.web.etc.sta.que.resize.Resize_Args;
import com.example.web.etc.sta.que.resize.Resize_que;
@RestController



public class sendFile {
	@Autowired
	UploadFileService uploadFileService;
	
	@Autowired
	Upload_hashService upload_hashService;
	
	@Autowired
	extensionUploadService exService;

	
	@PostMapping("/anime-web/uploader/sendFile")
	public Boolean start2(@RequestParam String name, @RequestParam MultipartFile file,@RequestParam Boolean Isduplicate,HttpSession session) {
		 Integer userId=(Integer) session.getAttribute("id");
			
		 if (userId==null) {
			 System.out.println("セッションが切断されています");
			 throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
			//return false;
		 }
		 
		 
		
   
        // MIMEタイプを取得
        String mimeType=file.getContentType();
        System.out.println(mimeType);
        
		 extensionUpload  uploadFileType=exService.selectOne(mimeType);
		 
		//画像のアップロード先
	     String fullPath = Setting.getRoot();
	     Path folder;
			
		 
	    // fileType filetype;
	     //System.out.println(exListImage.);
		 if (!(uploadFileType.getType()==null)) {
			 folder = Paths.get(fullPath+ "content\\anime-web\\upload\\file\\"+uploadFileType.getType()).normalize(); 
	

		 }else {
			 folder = Paths.get(fullPath+ "content\\anime-web\\upload\\file\\"+"other").normalize(); 
				
		 }
		 
		 String fname = RemoveExtension.main(name);
		 String lname=GetExtension.main(name);
		 
		 Integer exId=uploadFileType.getExId();
		 String alias=uploadFileService.insertFile(fname,lname, mimeType, userId,exId);
		 String inputPathStr=folder+"\\" + alias;
			
		 Path inputPath = Paths.get(inputPathStr).normalize();

         try {
        	//ファイルを保存
        	 SaveFile.main(file,inputPath.toString());
        	// byte[] fBytes =GetBytes.getFileBytes(file);
        	 String hash=ToHash256.hashWithFile(inputPath.toFile());
        	 
        	 if(upload_hashService.count(userId, hash)>0&&Isduplicate) {
        		 uploadFileService.delete(userId.toString(), alias);
        		 DeleteR.main(inputPath, false);
        		 throw new ResponseStatusException(HttpStatus.CONFLICT, "ファイルが重複しています");
        	 }
        	 
        	
        	 
        	 
        	/* 
        	 try (FileOutputStream outputStream = new FileOutputStream(inputPath.toString())) {
                 outputStream.write(fBytes);
             }
        	 */

        	 
        	 upload_hashService.insertHash(alias, hash);
        	 
        	 Path audioOutput=Paths.get(fullPath,"content","anime-web","upload","file","audio",alias+".flac");
		        

			//動画はサムネとHLS
			if(Objects.equals( fileType.VIDEO.ordinal(), uploadFileType.getTypeId()) ) {
		        Path dir = Paths.get(fullPath,"content", "anime-web", "upload", "file","hls",alias).normalize();     
		        Files.createDirectories(dir);
		        
		        //String url=Setting.getUrl()+"content/anime-web/upload/file/hls/"+alias;
		        URI  m3u8Url=new URI ("/anime-web/getFile/view/video/hls/"+alias).normalize();
		        
		        //キューに追加
		        createEncodeAudio(inputPath.toString(),fullPath,alias,audioOutput.toString());
		        
		        
		        Hls_Que hlsQue = new Hls_Que();
		        ArgsData hlsArgs = new HlsArgs(inputPathStr,dir,m3u8Url.toString(), hlsQue, audioOutput.toString());
		        Que.addToQueue(hlsArgs);
		        
		        createSeekImage(inputPath.toString(),fullPath,alias);
		       
				//createThumbnailVideo(inputPath.toString(),fullPath,alias);
				
				//画像はサムネ
			}else if(Objects.equals(fileType.IMAGE.ordinal(),  uploadFileType.getTypeId()) ) {
				System.out.println(inputPath.toString());
				createThumbnailImage(inputPath.toString(),fullPath,alias,mimeType);

			}else if(Objects.equals(fileType.AUDIO.ordinal(),  uploadFileType.getTypeId()) ) {
				createEncodeAudio(inputPath.toString(),fullPath,alias,audioOutput.toString());

			}
			return alias==null?false:true;
			
		 }catch (IOException | URISyntaxException | NoSuchAlgorithmException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		 }
		
		

		
		
	}
	
	
	private static void createThumbnailImage(String input,String fullPath,String alias,String mimeType) {
		//cpu 
		//String p ="ffmpeg -i \"{0}\"   -vf \"scale=if(gt(iw\\,ih)\\,220\\,-2):if(gt(iw\\,ih)\\,-2\\,220)\"  -compression_level 6 -q:v 50  -pix_fmt yuv420p \"{1}\"" ;
		 //Path output =  Paths.get("content\\anime-web\\upload\\file\\thumbnail\\"+alias+".avif").normalize();
		 //String format= MessageFormat.format(p,input,output);	
		 //ExecProcess.main(format);
		 Img img=new Img(new File(input));
		 Path tempPath =  Paths.get(Setting.getRoot()+"content\\anime-web\\upload\\file\\thumbnail-temp\\"+alias+".avif").normalize();
		 
		 //1024以上の画像を1024にリサイズ
		 if(img.getHeight()>1024 || img.getWidth()>1024) {
			 resize(1024,mimeType,input, tempPath,"thumbnail-big",alias);

		 }
		 //324以上は324にリサイズし、これをサムネイルにする
		 if(img.getHeight()>324 || img.getWidth()>324) {

			 resize(324,mimeType,input, tempPath,"thumbnail",alias);
		
		 }else {//小さい画像はリサイズせずに圧縮してサムネイルにする

			 Integer biggestSize =img.getHeight()>img.getWidth()?img.getHeight():img.getWidth();
			 
			//avifで圧縮
			compressImg(new File(input),"thumbnail",biggestSize,alias);
		 }

		
		 img=null;
		// ResizeImg.main(new File(input), output, 200,mimeType);//サイズが大きいとエラーになるため別で処理
	}
	private static void resize(Integer imgSize,String mimeType,String input,Path tempPath,String outputPath,String alias) {
		File temp=new File(tempPath.toAbsolutePath().toString()+imgSize.toString()+".png");
		//リサイズしてPNG形式に変換
		Resize_que resizeQue= new Resize_que();
		ArgsData resizeArgs = new Resize_Args(input,temp.getAbsolutePath(), imgSize, mimeType, resizeQue);
		Que.addToQueue(resizeArgs);
			 
		//avifで圧縮
		compressImg(temp,outputPath,imgSize,alias);
		return;
 
	}
	private static void compressImg(File inputPath,String outputPath,Integer imgSize,String alias) {
		//avifに変換
		String p ="ffmpeg -i \"{0}\"    -vf \"scale=if(gt(iw\\,ih)\\,{2,number,#}\\,-2):if(gt(iw\\,ih)\\,-2\\,{2,number,#})\"  -c:v libsvtav1 -preset 8 -crf 50 \"{1}\"" ;
		Path output =  Paths.get(MessageFormat.format(Setting.getRoot()+"content\\anime-web\\upload\\file\\{0}\\"+alias+".avif",outputPath)).normalize();
		String format;
		format= MessageFormat.format(p,inputPath.getAbsolutePath().toString(),output.toString(),imgSize);
	
		Cmd_que cmdQue = new Cmd_que();
		ArgsData cmdArgs = new Cmd_Args(format, cmdQue);
		Que.addToQueue(cmdArgs);
	}
	private static void createThumbnailVideo(String input,String fullPath,String alias) {
		 
		String p ="ffmpeg -i \"{0}\"  -ss 1  -vframes 1 -vf \"scale=if(gt(iw\\,ih)\\,440\\,-2):if(gt(iw\\,ih)\\,-2\\,440)\"  -c:v libsvtav1 -preset 8 -crf 30 \"{1}\"" ;
		 
		 String output = fullPath+ "content\\anime-web\\upload\\file\\thumbnail\\"+alias+".avif";
		 String format= MessageFormat.format(p,input,output);	
		 
		 
		 Cmd_que cmdQue = new Cmd_que();
	     ArgsData cmdArgs = new Cmd_Args(format, cmdQue);
	     Que.addToQueue(cmdArgs);
		 
		 
		 
	}
	private static void createSeekImage(String input,String fullPath,String alias) {
		 
		String p ="ffmpeg -i \"{0}\"  -r 1 -vf \"scale=-1:128\"  -f image2  \"{1}\"" ;
        Path dir = Paths.get(fullPath,"content", "anime-web", "upload", "file", "seek-image",alias).normalize();
        try {
			Files.createDirectories(dir);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} 
		
		
		 String output = fullPath+ "content\\anime-web\\upload\\file\\seek-image\\"+alias+"\\"+"%08d.jpg";
		 String format= MessageFormat.format(p,input,output);	
		 
		 
		 Cmd_que cmdQue = new Cmd_que();
	     ArgsData cmdArgs = new Cmd_Args(format, cmdQue);
	     SecondQue.addToQueue(cmdArgs);//エンコードとは別のスレッドで実行
		 
	     
	     Thumbnail_que que = new Thumbnail_que();
	     ArgsData args = new Thumbnail_Args(alias,"max.jpg",que);
	     SecondQue.addToQueue(args);

		 
		 
	}
	private static void createEncodeAudio(String input,String fullPath,String alias,String outputPath) {
		 
		String p ="ffmpeg -i \"{0}\" -filter:a loudnorm=I=-10:LRA=11:TP=-1.5  -preset ultrafast -vn -threads 4 -c:a flac \"{1}\"" ;


		// String output = fullPath+ "content\\anime-web\\upload\\file\\audio\\"+alias+".flac";
		 String format= MessageFormat.format(p,input,outputPath);	
		 
		 Cmd_que cmdQue = new Cmd_que();
	     ArgsData cmdArgs = new Cmd_Args(format, cmdQue);
	     Que.addToQueue(cmdArgs);
	}
		
}

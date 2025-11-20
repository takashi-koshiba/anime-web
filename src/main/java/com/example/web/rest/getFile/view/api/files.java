package com.example.web.rest.getFile.view.api;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.Columns;
import com.example.web.etc.sta.Kakasi;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.Setting;
import com.example.web.etc.sta.SimilarWards;
import com.example.web.etc.sta.TextRep;
import com.example.web.etc.sta.uplaodColumn;
import com.example.web.etc.sta.uplaodColumn.orderEnum;
import com.example.web.etc.sta.que.ArgsData;
import com.example.web.etc.sta.que.Que;
import com.example.web.etc.sta.que.createMaxThumbnail.Thumbnail_Args;
import com.example.web.etc.sta.que.createMaxThumbnail.Thumbnail_que;
import com.example.web.uploader.sendFile.fileType;

@RestController
public class files {
	@Autowired
	UploadFileService uploadFileService;
	int maxLen=2;
	
	//検索にヒットするファイルを取得
	@GetMapping("/anime-web/getFile/view/api/file")
	private List<FileInfo> calcDistance(@RequestParam(required = false) String inputStr,HttpSession session,@RequestParam(required = false) Integer column,
			@RequestParam(required = false)  Integer ftype,Integer sort) {
		if(session.getAttribute("id")==null) {

			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "セッションがありません。");
		}
		
		
		List<Columns> listColumns=uplaodColumn.getColumnList();
		Integer columnId=listColumns.get(column).getOrderId();
		orderEnum orderText=orderEnum.values()[sort];
		
		String  userId=session.getAttribute("id").toString();
		String inputText=Kakasi.main(TextRep.main(inputStr,true),"-KH");
		
		inputText = inputText.replace("　", " ");
		inputText = inputText.replaceAll(" {2,}", " ");
		inputText = inputText.trim();
		
		String[] inputs = inputText.split(" ");
		Integer[] inputLen = new Integer[inputs.length];
		Boolean[] isShort = new Boolean[inputs.length];
		Integer[] maxCost = new Integer[inputs.length];
		String[][] splitStr = new String[inputs.length][]; // 内側の長さは後で設定

		for (int i = 0; i < inputs.length; i++) {
		    inputLen[i] = inputs[i].length();
		    isShort[i] = inputLen[i] < 4;
		    maxCost[i] = SimilarWards.maxLength(inputLen[i], isShort[i],maxLen);
		    splitStr[i] = SimilarWards.splitStr(inputs[i], maxCost[i], isShort[i],maxLen);
		}
		
		
		if(inputStr.equals("")||inputStr.isEmpty()||inputStr.isBlank()) {
			 long startTime = System.currentTimeMillis();
			 Log.log(Level.INFO, "SQL実行開始："+startTime);
			//対象ユーザーのファイルを取得
			List<FileInfo> fileInfoList =  uploadFileService.selectFile(userId,columnId,orderText.name(),ftype.intValue());
			for(FileInfo f :fileInfoList) {
				f.setUrl(getUrl(f));
				
			}
			long endTime = System.currentTimeMillis();
			 Log.log(Level.INFO, "SQL実行時間：" + (endTime - startTime) + " ms");
			return fileInfoList ;
		}
		
		
		List<FileInfo> fileInfoList =  uploadFileService.selectFile(userId,columnId,orderText.name(),ftype);
		                               
		
		
		//一致率を計算

		
		List<FileInfo> calcDistanceList=new ArrayList<FileInfo>();
		for(Integer i=0;i<fileInfoList.size();i++) {
			FileInfo fileInfo=fileInfoList.get(i);
			String searchTxt=fileInfo.getSearchTxt();
			
			Double dist=0d;
			

			
			for(Integer j=0;j<inputs.length;j++) {
				dist+=SimilarWards.exec(searchTxt,maxCost[j],splitStr[j],isShort[j],maxLen)*((double)1/(double)inputs.length);
			}
			dist=dist/(double) inputs.length;

			if(dist>0) {
				FileInfo f=new FileInfo();
				f.setId(fileInfo.getId());
				f.setFname(fileInfo.getFname());
				f.setLname(fileInfo.getLname());
				f.setAlias(fileInfo.getAlias());
				f.setRate(dist);
				f.setType(fileInfo.getType());
				f.setUrl(getUrl(fileInfo));
				
				
				calcDistanceList.add(f);
			}
			
		}
		//並び替え
		calcDistanceList.sort(Comparator.comparingDouble(item -> ((FileInfo) item).getRate()).reversed());
		
		
		return calcDistanceList;
		
	}
	
	private String getUrl(FileInfo fileInfo) {
		String url=fileInfo.getType()==fileType.IMAGE?getImageUrl(fileInfo):videoUrl(fileInfo);
		return url;
	}
	
	private String getImageUrl(FileInfo fileInfo) {
		
	    /* 重いので中止
	    if (!Files.exists(imagePath) || !Files.isRegularFile(imagePath) || !Files.isReadable(imagePath)) {
	    //    Log.log(Level.WARNING, "パスにアクセスできません。:"+imagePath);
	    	return "/anime-web/get-file/view/image/noImage/NoImage";
	    }
	    */

	    String urlPrefix =	"/anime-web/get-file/anime/image/small/";
	    
	    
	    return urlPrefix + fileInfo.getAlias();
	}
	private String videoUrl(FileInfo fileInfo) {

	    String fname="max.webp";
	    //サムネがなければ生成
	    Path maxImagePath = Paths.get(Setting.getRoot(),"content","anime-web","upload","file","maxSeek",fileInfo.getAlias()).resolve(fname).toAbsolutePath().normalize();
	    if (!Files.exists( maxImagePath)) {
	    	fname="max.jpg";
	    	maxImagePath = Paths.get(Setting.getRoot(),"content","anime-web","upload","file","maxSeek",fileInfo.getAlias()).resolve(fname).toAbsolutePath().normalize();
		    
	    }
	    
	    if (!Files.exists( maxImagePath)) {

		    Thumbnail_que que = new Thumbnail_que();
	        ArgsData args = new Thumbnail_Args(fileInfo.getAlias(),fname,que);
	        Que.addToQueue(args);
	        return getImageUrl(fileInfo); // 画像がないときの代替処理
		}

		

		//"/anime-web/get-file/anime/image/seekMax/{alias}"
		return String.join("/", "", "anime-web", "get-file", "anime", "image", "seekMax", fileInfo.getAlias(),fname);

		
	}
}


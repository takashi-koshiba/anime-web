package com.example.web.rest.getFile.view.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.uploadFile.FileInfo;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.sta.CalcCost;
import com.example.web.etc.sta.Columns;
import com.example.web.etc.sta.Kakasi;
import com.example.web.etc.sta.TextRep;
import com.example.web.etc.sta.uplaodColumn;
import com.example.web.etc.sta.uplaodColumn.orderEnum;

@RestController
public class files {
	@Autowired
	UploadFileService uploadFileService;

	
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
		String inputText=Kakasi.main(TextRep.main(inputStr),"-KH");
		
		Integer inputLen=inputText.length();
		Boolean isShort=inputLen<4;
		Integer maxCost=CalcCost.maxCost(inputLen,isShort);
		
		String[] splitStr=CalcCost.splitStr(inputText,maxCost,isShort);
		
		if(inputStr.equals("")||inputStr.isEmpty()||inputStr.isBlank()) {
			
			//対象ユーザーのファイルを取得
			List<FileInfo> fileInfoList =  uploadFileService.selectFile(userId,columnId,orderText.name(),ftype.intValue());
		
			return fileInfoList ;
		}
		
		
		List<FileInfo> fileInfoList =  uploadFileService.selectFile(userId,columnId,orderText.name(),ftype);
		                               
		
		
		//一致率を計算
		List<FileInfo> calcDistanceList=new ArrayList<FileInfo>();
		for(Integer i=0;i<fileInfoList.size();i++) {
			FileInfo fileInfo=fileInfoList.get(i);
			String searchTxt=fileInfo.getSearchTxt();
			Double rate =CalcCost.getMaxCost(searchTxt,maxCost,splitStr,isShort);

		
			if(rate>0) {
				FileInfo f=new FileInfo();
				f.setId(fileInfo.getId());
				f.setFname(fileInfo.getFname());
				f.setLname(fileInfo.getLname());
				f.setAlias(fileInfo.getAlias());
				f.setRate(rate);
				f.setType(fileInfo.getType());
				
				calcDistanceList.add(f);
			}
			
		}
		//並び替え
		calcDistanceList.sort(Comparator.comparingDouble(item -> ((FileInfo) item).getRate()).reversed());
		
		
		return calcDistanceList;
		
	}
}


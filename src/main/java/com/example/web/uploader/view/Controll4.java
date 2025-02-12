package com.example.web.uploader.view;



import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.etc.db.upload.UploadService;
import com.example.web.etc.db.uploadFile.UploadFileService;
import com.example.web.etc.db.uploadFile.Type.TypeService;
import com.example.web.etc.sta.Columns;
import com.example.web.etc.sta.uplaodColumn;
import com.example.web.etc.sta.uplaodColumn.sortColumnEnum;


@Controller
public class Controll4 {
	@Autowired
	UploadService uploadService;
	
	@Autowired
	UploadFileService uploadFileServise;
	
	
	@Autowired
	TypeService typeService;
	

/*
	@GetMapping("/anime-web/uploader/view/page/{page}")
	public ModelAndView   start(HttpSession session,@PathVariable Integer page) {
		
		return exec(-1,"",0,0,session,page);
		
	}
*/
	/*
	@GetMapping("/anime-web/uploader/view")
	public ModelAndView   start(HttpSession session) {
		
		return exec(-1,"",0,0,session,1);
		
	}
	*/
	@GetMapping("/anime-web/uploader/view/")
	public String  start1() {
		return "redirect:/anime-web/uploader/view";
	}
	/*
	@PostMapping("/anime-web/uploader/view/page/{page}")
	public String  start2() {
		return "redirect:/anime-web/uploader/view";
	}
*/
	@GetMapping("/anime-web/uploader/view")
	public ModelAndView   start3(@RequestParam(required = false) String fname,
			@RequestParam(required = false) Integer ftype,@RequestParam(required = false) Integer sort,
			@RequestParam(required = false) Integer column,@RequestParam(required = false) Integer page,HttpSession session) {
		
		Integer Ftype =ftype==null?-1:ftype;
		String Fname=fname==null?"":fname;
		Integer Sort=sort==null?0:sort;
		Integer Column=column==null?1:column;
		Integer Page=page==null?0:page;
		System.out.println(Fname);
		return exec(Ftype,Fname,Sort,Column,session,Page);
		
	}
	
	public ModelAndView exec(Integer ftype,String fname,Integer sort,Integer column,HttpSession session,Integer page) {
		if(session.getAttribute("id")==null) {

			ModelAndView model= new ModelAndView("redirect:/anime-web/uploader/login");
			return model;
		}
		String  id=session.getAttribute("id").toString();
		ModelAndView model= new ModelAndView("anime-web/uploader/view");
		model.addObject("users", uploadService.selectOne(id));

		
		//フォームに入力値を渡す
		int selectedSort=sort==null?1:sort;
		model.addObject("selectedType", ftype);
		
		model.addObject("selectedSort",selectedSort);
		model.addObject("fname", fname);
		
		model.addObject("selectedColumn", column);
		model.addObject("selectedColumns", getSortColumnList());
		model.addObject("types", typeService.selectAll());//ファイルタイプ


		
		
		String videoThumnailPath="/anime-web/get-file/anime/image/upthumbnail/";
		model.addObject("videoThumnailPath", videoThumnailPath );
		
		//model.addObject("pages",page(page,maxPages));
		
		return model;
	}
	private List<Type> getSortColumnList(){
		List<Type> listType= new ArrayList<>();
		

		List<Columns>listColumns=uplaodColumn.getColumnList();
		listType.add(new Type(sortColumnEnum.DATE.ordinal() ,listColumns.get(sortColumnEnum.DATE.ordinal()).getColumnName()));
		
		listType.add(new Type(sortColumnEnum.NAME.ordinal() ,listColumns.get(sortColumnEnum.NAME.ordinal()).getColumnName()));
		
		return listType;
	}
	/*
	private List<FileInfo>  search(Integer ftype,String fname,Integer sort,Integer column,ModelAndView model,String userId,Integer page){
		
		//ファイル名が空白
		List<Columns> listColumns=uplaodColumn.getColumnList();
		Integer columnId=listColumns.get(column).getOrderId();
		orderEnum orderText=orderEnum.values()[sort];
		if(fname.equals("")||fname.isEmpty()||fname.isBlank()) {
			
			//対象ユーザーのファイルを取得
			List<FileInfo> fileInfoList =  uploadFileServise.selectFile(userId,columnId,orderText.name(),ftype.intValue());
		
			return fileInfoList ;
		}
		
		//ファイル名で検索
		//calcDistance(fname,userId,columnId,ftype,orderText);
		//System.out.println(calcDistance(fname,userId,columnId,ftype,orderText));
		return calcDistance(fname,userId,columnId,ftype,orderText,page);
	}
	private List<FileInfo> calcDistance(String inputStr,String userId,Integer columnId,Integer ftype,orderEnum orderText,Integer page) {
		String inputText=TextRep.main(inputStr);
		
		Integer maxCost=CalcCost.maxCost(inputText.length());
		
		String[] splitStr=CalcCost.splitStr(inputText,maxCost);
		
		List<FileInfo> fileInfoList =  uploadFileServise.selectFile(userId,columnId,orderText.name(),ftype);
		
		
		
		//一致率を計算
		List<FileInfo> calcDistanceList=new ArrayList<FileInfo>();
		for(Integer i=0;i<fileInfoList.size();i++) {
			FileInfo fileInfo=fileInfoList.get(i);
			String searchTxt=fileInfo.getSearchTxt();
			Double rate =CalcCost.getMaxCost(searchTxt,maxCost,splitStr);

			
			if(rate>0) {
				FileInfo f=new FileInfo();
				f.setId(fileInfo.getId());
				f.setFname(fileInfo.getFname());
				f.setLname(fileInfo.getLname());
				f.setAlias(fileInfo.getAlias());
				f.setRate(rate);
				
				calcDistanceList.add(f);
			}
			
		}
		//並び替え
		calcDistanceList.sort(Comparator.comparingDouble(item -> ((FileInfo) item).getRate()).reversed());
		
		
		return calcDistanceList;
		
	}*/
	public Page page(Integer prevNum,Integer maxNum) {
		Page page=new Page();
		String urlStr;
		Integer pageQty=5;
		Integer pageHalf=pageQty/2;
		try {
			urlStr="http://localhost:8080/anime-web/uploader/view/page/";
			//url = URI.create("/anime-web/uploader/view/page/"+prevNum+1).toURL();
		 
			//次のページ
			if(prevNum<maxNum) {
				URL url;
	
				url=URI.create(urlStr+(prevNum+1)).toURL();
				page.setNextUrl(url.toString());
				page.setNextNum(prevNum+1);
			}
			if(prevNum>1) {
				URL url;
				url=URI.create(urlStr+(prevNum-1)).toURL();
				page.setPrevUrl(url.toString());
				page.setPrevNum(prevNum-1);
			}
			
			
			Integer minPos;
			
			if(prevNum<pageQty-1) {
				minPos=1;
			}else if(maxNum-prevNum<pageHalf) {
				minPos=maxNum-(pageQty-1);
			}else {
				minPos=prevNum-pageHalf;
			}
			
			Integer maxPos;
			maxPos=minPos+pageQty-1>maxNum?maxNum:minPos+pageQty-1;
			
			List<PageNumbers> pageList= new ArrayList<PageNumbers> ();
			
			for(Integer i=minPos;i<=maxPos;i++) {
				PageNumbers pageNum=new PageNumbers();
				pageNum.setPageNum(i);
				pageNum.setPageUrl(URI.create(urlStr+i).toURL());
				pageList.add(pageNum);
			}
			page.setPrevNum(prevNum);
			page.setPageNumbers(pageList);
			
			
		}
		catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return page;
		
	}
}

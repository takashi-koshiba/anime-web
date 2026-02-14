package com.example.web.rest.db.vectorAPI;
import java.util.List;
import java.util.logging.Level;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.animeVector.insert.AnimeInsertService;
import com.example.web.etc.db.animeVector.select.AnimeStrVector;
import com.example.web.etc.db.fulltext_search.videoTitle.insertDB.InsertTitle;
import com.example.web.etc.db.vector.VecDB;
import com.example.web.etc.sta.Log;
import com.example.web.index.BeanUser;
import com.example.web.index.GetIP;

@RestController
public class AnimeVector {


    //@Autowired
  //  ProgStrVector  progVector;
    @Autowired 
    AnimeStrVector animeStrVector;
    
    @Autowired
    AnimeInsertService animeInsertService;

    @Autowired
    InsertTitle insertTitle;
    
    @Autowired
    AnimeService animeService;
    
	//ProgInsertService progInsertService;
    int tableId=1;
    
	@GetMapping("/anime-web/api/db/vectorAPI/AnimeVector/{limitter}")
	public void  PlayList(@PathVariable  Integer limitter,HttpServletRequest request) throws IllegalAccessException {
		BeanUser user =GetIP.GetNameAndIp(request);
		if(!user.isAdmin()) throw new IllegalAccessException();
		
		//List<BigInteger> a =		vec.selectVecParent(0.07289126254330244 , 528);
		
	
		//BigInteger id = strVector.insertStrVecParent(0, 0);
		//strVector.InsertTxt("", 0,id);
		
		//strVector.selectStr("", 2);
		//progVector.selectStr("", 2);
		//全書き込み
		Log.log(Level.INFO, "ベクトル書き込みスタート");
		insertTitle.InsertOne();
		Log.log(Level.INFO, "ベクトル書き込み終了");
		//progInsertService.insertTitle(limitter);

	}
	@PostMapping("/anime-web/api/db/vectorAPI/AnimeVector/{limitter}")
	public void  PlayList2(@PathVariable  Integer limitter,HttpServletRequest request) throws IllegalAccessException {

		PlayList(limitter,request);
		
	}
	@GetMapping("/anime-web/api/db/vectorAPI/AnimeVector/search/{str}")
	public List<VecDB>  PlayList3(@PathVariable String str) {
		return animeStrVector.selectStr(str, tableId,-1);
	}
	@GetMapping("/anime-web/api/db/vectorAPI/AnimeVector/showCount")
	public int   PlayList4() {
		return animeService.countUnhashedAnime();
		
	}
	@PostMapping("/anime-web/api/db/vectorAPI/AnimeVector/showCount")
	public int   PlayList5() {
		return PlayList4();
		
	}
}
package com.example.web.rest.db.vectorAPI;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.animeVector.select.AnimeSelectSearvice;
import com.example.web.etc.db.fulltext_search.prog.insertDB.progInsertTitle;
import com.example.web.etc.db.progVector.insert.ProgInsertService;
import com.example.web.index.BeanUser;
import com.example.web.index.GetIP;

@RestController
public class ProgInsertApi {


    @Autowired
    progInsertTitle  progInsert;
    
    
    @Autowired
    AnimeSelectSearvice animeVectorService;
	@Autowired
	ProgInsertService progInsertService;
    
	@GetMapping("/anime-web/api/db/vectorAPI/progInsert/{limitter}")//@PathVariable String id
	public void  PlayList(@PathVariable  Integer limitter,HttpServletRequest request) throws IllegalAccessException {
		BeanUser user =GetIP.GetNameAndIp(request);
		if(!user.isAdmin()) throw new IllegalAccessException();
		
		//List<BigInteger> a =		vec.selectVecParent(00, 528);
		
	
		//BigInteger id = strVector.insertStrVecParent(0, 0);
		//strVector.InsertTxt("", 0,id);
		
		//strVector.selectStr("", 2);
		//progVector.selectStr("", 2);
		//全書き込み
		//animeVectorService.insertTitle();
		//progInsertService.insertTitle(limitter);
		progInsert.InsertOne();

	}
	@PostMapping("/anime-web/api/db/vectorAPI/progInsert/{limitter}")//@PathVariable String id
	public void  PlayList2(@PathVariable  Integer limitter,HttpServletRequest request) throws IllegalAccessException {
		PlayList(limitter,request);

	}
	
	@PostMapping("/anime-web/api/db/vectorAPI/progInsert/showCount")//@PathVariable String id
	public Long  PlayList3(HttpServletRequest request) throws IllegalAccessException {
		return (long) progInsert.count();

	}
	
	
	
}
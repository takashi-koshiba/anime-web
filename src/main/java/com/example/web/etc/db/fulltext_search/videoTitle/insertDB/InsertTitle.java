package com.example.web.etc.db.fulltext_search.videoTitle.insertDB;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.fulltext_search.videoTitle.doc.DocService;
import com.example.web.etc.db.fulltext_search.videoTitle.hash.HashService;
import com.example.web.etc.db.fulltext_search.videoTitle.parent.ParentService;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.NgramHasher;

@Service
public class InsertTitle {
	@Autowired
	AnimeService animeService;
	
	@Autowired
	ParentService parentService;
	
	@Autowired
	DocService docService;
	
	@Autowired
	HashService hashService;
	
	int lenNgram=2;
	
	@Transactional
	public void Insert(Anime anime) {
		try {
			List<String> hashTargets = new ArrayList<String>();
			long parentId=parentService.insert(anime.getId());
			
			List<Anime> alias = animeService.selectAliasOne(anime.getId());
			
			hashTargets.add(anime.getFoldername());
			hashTargets.add(anime.getOriginalName());
			
			for (Anime a : alias) {
			    hashTargets.add(a.getOriginalName());
			}
			
			//文字ごとに書き込み
			for(String str : hashTargets) {
				long docId= docService.insert(parentId);
				insertToHash(docId,str);
			}
		}catch (DataAccessException e) {
			Log.detail(Level.WARNING, "InsertTitleでエラー", e);
			throw e;
			
		}
		
		
		
		
		
	}
	
	private  void insertToHash(long docId,String str) {
		
		
		String[] ngramStr=NgramHasher.strToArr(str,lenNgram);
		int i=0;
		for(String s : ngramStr) {
			
			long hash = NgramHasher.strToVecNgram1D(s,lenNgram);
			hashService.insert(docId, hash, i);
			Log.log(Level.INFO, "番組タイトルのインデックスを登録しました。"+s);
			i++;
		}
		
		
		 
	}
	
	public void InsertOne(int animeId) {
		List<Anime> anime= animeService.selectUnhashedAnime(animeId);

		Insert(anime.getFirst());
	}
	
	public void  InsertOne() {
		List<Anime> anime= animeService.selectUnhashedAnime();
		Insert(anime.getFirst());
	}
	
	public void AllInsert() {
		List<Anime> anime= animeService.selectUnhashedAnime();
		
		for (Anime a:anime) {
		
			
			Insert(a);
		}
		

	}
	


}

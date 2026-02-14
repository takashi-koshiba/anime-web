package com.example.web.etc.db.fulltext_search.prog.insertDB;

import java.util.List;
import java.util.logging.Level;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.fulltext_search.prog.doc.progDocService;
import com.example.web.etc.db.fulltext_search.prog.hash.progHashService;
import com.example.web.etc.db.fulltext_search.prog.parent.progParentService;
import com.example.web.etc.db.fulltext_search.prog.parent.video_prog;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.NgramHasher;

@Service
public class progInsertTitle {
	@Autowired
	AnimeService animeService;
	
	@Autowired
	progParentService parentService;
	
	@Autowired
	progDocService docService;
	
	@Autowired
	progHashService hashService;
	
	int lenNgram=2;
	
	@Transactional
	private void Insert(video_prog progs) {
		try {

			//parentID発行
			long parentId=parentService.insert(progs.getId());
			
			//docID発行
			long docId= docService.insert(parentId);

			insertToHash(docId,progs.getTxt());
			
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
	

	
	public void  InsertOne() {
		List<video_prog> video_progs= parentService.selectUnhashedParentId(true);
		
		Insert(video_progs.getFirst());
	}
	public int count() {
		List<video_prog> video_progs= parentService.selectUnhashedParentId(false);
		return video_progs.size();
	}
	
	public void AllInsert() {
		List<video_prog> video_progs= parentService.selectUnhashedParentId(false);
		
		
		for (video_prog a:video_progs) {
		
			
			Insert(a);
		}
		

	}
	


}

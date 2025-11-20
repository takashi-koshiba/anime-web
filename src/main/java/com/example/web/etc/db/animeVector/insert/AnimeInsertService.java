package com.example.web.etc.db.animeVector.insert;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class AnimeInsertService {
    private final AnimeInsertJDBC progVectorJDBC;

    // コンストラクタインジェクション
 
    public AnimeInsertService(AnimeInsertJDBC animeJDBC) {
        this.progVectorJDBC = animeJDBC;
    }
    
    public void  insertTitle(int limitter) {
    	this.progVectorJDBC.insertTitle(limitter);
    }
    public void delAnimeVector(int parentId) {
    	this.progVectorJDBC.delAnimeVector(parentId);
    }
    public int showCount() {
    	return this.progVectorJDBC.showCount();
    }
}

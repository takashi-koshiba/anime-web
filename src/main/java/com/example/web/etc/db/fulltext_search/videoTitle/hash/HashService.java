package com.example.web.etc.db.fulltext_search.videoTitle.hash;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class HashService {
    private final HashJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public HashService(HashJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }


    
    public void insert(long doc_id,long hash,int pos ) {
    	animeJDBC.insert(doc_id,hash,pos);
    }
 
}

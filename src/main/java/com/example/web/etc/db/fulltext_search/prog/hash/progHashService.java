package com.example.web.etc.db.fulltext_search.prog.hash;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class progHashService {
    private final progHashJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public progHashService(progHashJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }


    
    public void insert(long doc_id,long hash,int pos ) {
    	animeJDBC.insert(doc_id,hash,pos);
    }
 
}

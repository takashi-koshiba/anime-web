package com.example.web.etc.db.progVector.insert;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class ProgInsertService {
    private final ProgVectorJDBC progVectorJDBC;

    // コンストラクタインジェクション
 
    public ProgInsertService(ProgVectorJDBC animeJDBC) {
        this.progVectorJDBC = animeJDBC;
    }
    
    public void  insertTitle(int limitter) {
    	this.progVectorJDBC.insertTitle(limitter);
    }
    public Long countSelect() {
    	return this.progVectorJDBC.countSelect();
    }
    
}

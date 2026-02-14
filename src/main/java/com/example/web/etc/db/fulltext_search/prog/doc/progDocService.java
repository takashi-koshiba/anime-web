package com.example.web.etc.db.fulltext_search.prog.doc;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class progDocService {
    private final progDocJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public progDocService(progDocJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }

    
    public List<progDoc> selectById(Long parentId){
    	return animeJDBC.selectById(parentId );
    }
    
    public Long insert(Long parentId) {
    	return animeJDBC.insert(parentId);
    }
 
}

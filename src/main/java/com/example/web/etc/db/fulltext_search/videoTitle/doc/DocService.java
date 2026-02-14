package com.example.web.etc.db.fulltext_search.videoTitle.doc;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class DocService {
    private final DocJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public DocService(DocJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }

    
    public List<Doc> selectById(Long parentId){
    	return animeJDBC.selectById(parentId );
    }
    
    public Long insert(Long parentId) {
    	return animeJDBC.insert(parentId);
    }
 
}

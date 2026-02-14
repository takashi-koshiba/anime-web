package com.example.web.etc.db.fulltext_search.videoTitle.parent;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class ParentService {
    private final ParentJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public ParentService(ParentJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }

    
    public List<Parent> selectById(int id ){
    	return animeJDBC.selectById(id );
    }
    
    public long insert(int id) {
    	return animeJDBC.insert(id);
    }
 
    
    public void delete (int animeId) {
    	animeJDBC.delete(animeId);
    }
}

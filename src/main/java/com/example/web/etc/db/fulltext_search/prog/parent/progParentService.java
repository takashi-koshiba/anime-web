package com.example.web.etc.db.fulltext_search.prog.parent;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class progParentService {
    private final progParentJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public progParentService(progParentJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }

    
    public List<progParent> selectById(int id ){
    	return animeJDBC.selectById(id );
    }
    
    public long insert(long id) {
    	return animeJDBC.insert(id);
    }
 
    
    public void delete (int animeId) {
    	animeJDBC.delete(animeId);
    }
	public List<video_prog> selectUnhashedParentId(boolean isLimit){
		return animeJDBC.selectUnhashedParentId(isLimit);
	};
}

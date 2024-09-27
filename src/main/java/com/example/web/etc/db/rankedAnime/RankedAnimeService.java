package com.example.web.etc.db.rankedAnime;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class RankedAnimeService {
    private final RankedAnimeJDBC rankedAnimeJDBC;

    // コンストラクタインジェクション
 
    public RankedAnimeService(RankedAnimeJDBC rankedAnimeJDBC) {
        this.rankedAnimeJDBC = rankedAnimeJDBC;
    }
/*
    public boolean IsExistItem(String item) {
        int count = animeJDBC.countRow(item);
        return count==0? false:true;
    }
    
    public boolean insert(Anime anime) {
    	int row=animeJDBC.insert(anime);
    	
    	return row>0?true:false;
    }
  */  
    
    public Integer countRow() {
    	return rankedAnimeJDBC.countRow(); 
    }
    
    public List<RankedAnime> selectAll(){
    	return rankedAnimeJDBC.selectAll(); 
    }
    
    public RankedAnime selectOne(Integer animeId) {
    	//return rankedAnimeJDBC.selectOne(3000);]
    	RankedAnime result = rankedAnimeJDBC.selectOne(animeId);
    	return result;
    	
    }

}

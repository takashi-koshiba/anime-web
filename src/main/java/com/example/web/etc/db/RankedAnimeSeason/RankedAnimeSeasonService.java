package com.example.web.etc.db.RankedAnimeSeason;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class RankedAnimeSeasonService {
    private final RankedAnimeSeasonJDBC rankedAnimeSeasonJDBC;

    // コンストラクタインジェクション
 
    public RankedAnimeSeasonService(RankedAnimeSeasonJDBC rankedAnimeSeasonJDBC) {
        this.rankedAnimeSeasonJDBC = rankedAnimeSeasonJDBC;
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
    public List<RankedAnimeSeason> selectAll(String year,String season){
    	return rankedAnimeSeasonJDBC.selectAll(Integer.parseInt(year),Integer.parseInt(season));
    }
    
    public List<RankedAnimeSeason> selectAll(String year){
    	return rankedAnimeSeasonJDBC.selectAll(Integer.parseInt(year));
    	
    }
    public List<RankedAnimeSeason> selectAll(){
    	return rankedAnimeSeasonJDBC.selectAll();
    	
    }

}

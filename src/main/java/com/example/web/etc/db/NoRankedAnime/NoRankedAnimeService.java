package com.example.web.etc.db.NoRankedAnime;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class NoRankedAnimeService {
    private final NoRankedAnimeJDBC noRankedAnimeJDBC;

    // コンストラクタインジェクション
 
    public NoRankedAnimeService(NoRankedAnimeJDBC noRankedAnimeJDBC) {
        this.noRankedAnimeJDBC = noRankedAnimeJDBC;
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
    public List<NoRankedAnime> selectAll(String year,String season){
    	return noRankedAnimeJDBC.selectAll(Integer.parseInt(year),Integer.parseInt(season));
    }
    
    public List<NoRankedAnime> selectAll(String year){
    	return noRankedAnimeJDBC.selectAll(Integer.parseInt(year));
    	
    }
    public List<NoRankedAnime> selectAll(){
    	return noRankedAnimeJDBC.selectAll();
    	
    }

}

package com.example.web.etc.db.rankedNewAnime;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class RankedNewAnimeService {
    private final RankedNewAnimeJDBC rankedNewAnimeJDBC;

    // コンストラクタインジェクション
 
    public RankedNewAnimeService(RankedNewAnimeJDBC rankedNewAnimeJDBC) {
        this.rankedNewAnimeJDBC = rankedNewAnimeJDBC;
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
    public  List<RankedNewAnime> selectAll(String year,String season){

    	return rankedNewAnimeJDBC.selectAll(Integer.parseInt(year),Integer.parseInt(season));
    }
    
    public List<RankedNewAnime> selectAll(String year){

    	return rankedNewAnimeJDBC.selectAll(Integer.parseInt(year));
    	
    }
    public List<RankedNewAnime> selectAll(){

    	return rankedNewAnimeJDBC.selectAll();
    	
    }

}

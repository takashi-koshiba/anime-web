package com.example.web.rest.animeimg.Animetable;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class AnimeService {
    private final AnimeJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public AnimeService(AnimeJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }

    public boolean IsExistItem(String item) {
        int count = animeJDBC.countRow(item);
        return count==0? false:true;
    }
    
    public boolean insert(Anime anime) {
    	int row=animeJDBC.insert(anime);
    	
    	return row>0?true:false;
    }
    


}
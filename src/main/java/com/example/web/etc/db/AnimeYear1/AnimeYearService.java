package com.example.web.etc.db.AnimeYear1;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class AnimeYearService {
    private final AnimeYearJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public AnimeYearService(AnimeYearJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }

    
    public List<AnimeYear> selectAll(){
    	return animeJDBC.selectAll();
    }
    


}

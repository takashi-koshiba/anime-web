package com.example.web.etc.db.fulltext_search.videoTitle.selectDB;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class AnimeHashService {
    private final AnimeHashJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public AnimeHashService(AnimeHashJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }
    
    public List<AnimeHash> selectByHash(String str) {
    	return animeJDBC.selectByHash(str);
    }
    
}

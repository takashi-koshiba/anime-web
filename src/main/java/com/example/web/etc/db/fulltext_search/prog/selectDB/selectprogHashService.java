package com.example.web.etc.db.fulltext_search.prog.selectDB;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class selectprogHashService {
    private final selectprogHashJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public selectprogHashService(selectprogHashJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }
    
    public List<selectprogHash> selectByHash(String str) {
    	return animeJDBC.selectByHash(str);
    }
    
}

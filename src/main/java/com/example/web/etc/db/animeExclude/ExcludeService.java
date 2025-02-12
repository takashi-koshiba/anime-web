package com.example.web.etc.db.animeExclude;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class ExcludeService {
    private final ExcludeJDBC animeJDBC;

    // コンストラクタインジェクション
 
    public ExcludeService(ExcludeJDBC animeJDBC) {
        this.animeJDBC = animeJDBC;
    }

    
    public List<Exclude> selectAll(){
    	return animeJDBC.selectAll();
    }
    
    public void insert(String title) {
    	animeJDBC.insert(title);
    }
    public void del() {
    	animeJDBC.del();
    }

}

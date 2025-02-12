package com.example.web.etc.db.renban;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class renbanService {
    private final renbanJDBC renbanJDBC;

    // コンストラクタインジェクション
 
    public renbanService(renbanJDBC renbanJDBC) {
        this.renbanJDBC = renbanJDBC;
    }

    
    
    public String selectOne(){
    	return renbanJDBC.selectOne();
    }
    
    public void Insert(String prefix,Integer id) {
    	 renbanJDBC.Insert(prefix, id);
    }
}

package com.example.web.etc.db.upload_hash;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class Upload_hashService {
    private final Upload_hashJDBC upload_hashJDBC;

    // コンストラクタインジェクション
 
    public Upload_hashService(Upload_hashJDBC upload_hashJDBC) {
        this.upload_hashJDBC = upload_hashJDBC;
    }

    
    public int count(Integer  id,String hash){
    	return upload_hashJDBC.count(id,hash);
    }
    public Boolean insertHash(String alias ,String hash){
    	return upload_hashJDBC.insertHash(alias,hash);
    }
}

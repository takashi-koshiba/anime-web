package com.example.web.etc.db.extensionUpload;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class extensionUploadService {
    private final extensionUploadJDBC extensionUploadJDBC;

    // コンストラクタインジェクション
 
    public extensionUploadService(extensionUploadJDBC extensionUploadJDBC) {
        this.extensionUploadJDBC = extensionUploadJDBC;
    }

    
    
    public extensionUpload selectOne(String mime){
    	return extensionUploadJDBC.selectOne(mime);
    }
    
 
}

package com.example.web.etc.db.upload;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class UploadService {
    private final UploadJDBC uploadJDBC;

    // コンストラクタインジェクション
 
    public UploadService(UploadJDBC uploadJDBC) {
        this.uploadJDBC = uploadJDBC;
    }

    
    public Upload selectOne(String id){
    	return uploadJDBC.selectOne(id);
    }
    public Upload login(Integer id,String pw){
    	return uploadJDBC.login(id,pw);
    }

    public List<Upload> selectUserAll(){
    	return uploadJDBC.selectUserAll();
    }
    public Boolean insertUser(String name,String pw){
    	return uploadJDBC.insertUser(name,pw);
    }
}

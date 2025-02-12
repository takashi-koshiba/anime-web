package com.example.web.etc.db.uploadFile;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class UploadFileService {
    private final UploadFileJDBC uploadFileJDBC;

    // コンストラクタインジェクション
 
    public UploadFileService(UploadFileJDBC uploadJDBC) {
        this.uploadFileJDBC = uploadJDBC;
    }

    
    
    public String insertFile(String fname,String lname,String mime,Integer id,Integer mimeId){
    	return uploadFileJDBC.insertFile(fname,lname,mime,id,mimeId);
    }
    
    public List<FileInfo> selectFile(String userId, Integer columnId, String order, Integer ftypeId) {
    	return uploadFileJDBC.selectFile(userId, columnId,  order,  ftypeId);
    }
    public List<FileInfo> selectFileOne(String userId,String fId){
    	return uploadFileJDBC.selectFileOne(userId, fId);
    }
    public Integer countRow(String userId,Integer ftypeId) {
    	return uploadFileJDBC.countRow(userId, ftypeId);
    }
    public Integer getItemLimit() {
    	return uploadFileJDBC.getItemLimit();
    }
    public void delete(String userId,String fid) {
    	uploadFileJDBC.delete(userId, fid);
    }
    
}

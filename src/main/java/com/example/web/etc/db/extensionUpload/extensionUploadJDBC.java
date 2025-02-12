package com.example.web.etc.db.extensionUpload;

import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class extensionUploadJDBC implements extensionUploadDao {

    @Autowired
    private JdbcTemplate jdbc;

    
    @Override
    @Transactional
    
    public extensionUpload selectOne(String mime){
    	try {
    		String sql= "select type.id as typeId,extension.id as exId,fileType,ex from type "
    				+ "join extension on type.id=extension.type "
    				+ "where concat(fileType,'/',ex)=? ";

        	
    		Map<String, Object> map = jdbc.queryForList(sql,mime).getFirst();
    			extensionUpload obj=new extensionUpload();
    			obj.setType((String)map.get("fileType"));
    			obj.setTypeId((Integer)map.get("typeId"));
    		    obj.setExId((Integer)map.get("exId"));
    			
    		
    		return obj;
    	}catch (Exception e) {
        	return new extensionUpload();
    	}
    }
    

}

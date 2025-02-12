package com.example.web.etc.db.upload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UploadJDBC implements UploadDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
	public Upload selectOne(String id) {
		try {
			String sql = "select name from up_user where id = ?";
			Map<String, Object> result = jdbc.queryForList(sql,id).getFirst();
        
			Upload upload = new Upload();
			upload.setName((String)result.get("name"));

			return upload;
    	}catch (Exception e) {
    		Upload upload = new Upload();
			return upload;
    	}
	}
    @Override
	public Upload login(Integer id,String pw) {
		try {
			String sql = "select name from up_user where id = ? and pw = ?";
			Map<String, Object> result = jdbc.queryForList(sql,id,pw).getFirst();
        
			Upload upload = new Upload();
			upload.setName((String)result.get("name"));

			return upload;
    	}catch (Exception e) {
    		Upload upload = new Upload();
			return upload;
    	}
	}
    @Override
    public  List<Upload> selectUserAll(){
    	try {
    		String sql = "select id,name from up_user order by name";
			List<Map<String, Object>> result = jdbc.queryForList(sql);
        
			List<Upload> list = new ArrayList<>();
			for(Map<String,Object>map:result) {
				Upload upload = new Upload();
        	
				upload.setName((String)map.get("name"));
				upload.setId((Integer)map.get("id"));
				list.add(upload);
			}

			return list;
    	}catch (Exception e) {
    		List<Upload> list = new ArrayList<>();
			return list;
    	}
    }
    
    @Override
    public Boolean insertUser(String name,String pw) {
    	try {
        	jdbc.update("INSERT INTO up_user (name, pw) VALUES(?, ?)",
                    name,pw);
        	
        	return true;
    	}catch(Exception e){
    		return false;
    	}

    }
    
}

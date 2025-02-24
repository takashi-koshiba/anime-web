package com.example.web.etc.db.upload_hash;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Upload_hashJDBC implements Upload_hashDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
	public int count(Integer id,String hash) {
		try {
			String sql = "select count(*) as c from upload_hash join uploadfile using(alias) where uploadfile.user_id = ? and hash = ? ";
			Map<String, Object> result = jdbc.queryForList(sql,id,hash).getFirst();
        
			return Integer.parseInt(result.get("c").toString()) ;
    	}catch (Exception e) {
    		e.printStackTrace();
			return 0;
    	}
	}
    
    @Override
    public Boolean insertHash(String alias ,String hash) {
    	try {
        	jdbc.update("INSERT INTO upload_hash (alias, hash) VALUES(?, ?)",
                   alias,hash);
        	
        	return true;
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}

    }
    
}

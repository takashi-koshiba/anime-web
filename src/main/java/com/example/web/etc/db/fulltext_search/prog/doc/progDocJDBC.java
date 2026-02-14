package com.example.web.etc.db.fulltext_search.prog.doc;


import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.web.etc.sta.Log;


@Repository
public class progDocJDBC implements progDocDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public long insert(long  parentId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        	jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO progdoc(parent_id) VALUES(?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, parentId);
            return ps;
        	}, keyHolder);
        	
   
        return keyHolder.getKey().longValue();
    }
    @Transactional
    public void  del(long parentId) {
    	 try {

      jdbc.update("delete from progdoc where parent_id = ?",parentId);

    		}catch (Exception e) {
    	 		Log.detail(Level.WARNING,"videotitledocでエラー",e);
        	}
       
    }

    
    
    
	@Override
	public List<progDoc> selectById(long id ) {
		List<progDoc> parentList = new ArrayList<>();
		try {
			String sql = "select id from progdoc where parent_id= ?";
			
			List<Map<String, Object>> result = jdbc.queryForList(sql,id);
			
			
			for(Map<String,Object>map:result) {
				progDoc parent = new progDoc();
				
				parent.setId(((Number) map.get("id")).longValue());

				parentList.add(parent);
			}
			
			
    	}catch (Exception e) {
	 		Log.detail(Level.WARNING,"progdocでエラー",e);
    	}
		return parentList;
		
	}

}

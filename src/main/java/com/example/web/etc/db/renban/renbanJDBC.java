package com.example.web.etc.db.renban;

import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class renbanJDBC implements renbanDao {

    @Autowired
    private JdbcTemplate jdbc;

    
    @Override
    @Transactional
    public String selectOne() {
    	try {
    		
    	
    		String sql = "select alias from renban order by alias limit 1";
    		Map<String, Object> result = jdbc.queryForList(sql).getFirst();
    		
    		String alias = (String) result.get("alias");
    		
    		 jdbc.update("delete from renban where alias =?", alias);
    		
    		return alias;
    	}catch (Exception e) {
        	return null;
    	}
    }
    @Override
    public void Insert(String prefix,Integer id) {
    	jdbc.update("insert into renban (alias) values(?)",prefix+id);
    }
    
}

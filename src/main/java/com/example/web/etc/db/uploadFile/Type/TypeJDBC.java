package com.example.web.etc.db.uploadFile.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TypeJDBC implements TypeDao {

    @Autowired
    private JdbcTemplate jdbc;

	
	
    @Override
    @Transactional
    public List<Type> selectAll(){
		try {
			
		
			String sql = "select id,type from type order by id";
			List<Map<String, Object>> result = jdbc.queryForList(sql);
        
			List<Type> list = new ArrayList<>();
			for(Map<String,Object>map:result) {
				Type type = new Type();
				type.setId((int)map.get("id"));
				type.setValue((String)map.get("type"));
				list.add(type);
			}
			return list;
    	}catch (Exception e) {
	 		List<Type> list = new ArrayList<>();
	 		list.add(new Type());
	 		return list;
    	}
	}
    
}

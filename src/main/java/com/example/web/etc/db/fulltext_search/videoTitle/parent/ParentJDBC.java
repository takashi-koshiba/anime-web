package com.example.web.etc.db.fulltext_search.videoTitle.parent;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.web.etc.sta.Log;

@Repository
public class ParentJDBC implements ParentDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public long insert(int animeId ) {   

        KeyHolder keyHolder = new GeneratedKeyHolder();

    	jdbc.update(con -> {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO videotitleparenthash(anime_id) VALUES(?)",
            Statement.RETURN_GENERATED_KEYS
        );
        ps.setLong(1, animeId);
        return ps;
    	}, keyHolder);
    	

    return keyHolder.getKey().longValue();
    }
    
    @Override
    public void delete(int animeid) {
    	 jdbc.update("delete from videotitleparenthash where anime_id =? ",animeid);

    }
    
	@Override
	public List<Parent> selectById(int animeId ) {
		List<Parent> parentList = new ArrayList<>();
		try {
			String sql = "select id from videotitleparenthash where anime_id= ?";
			
			List<Map<String, Object>> result = jdbc.queryForList(sql,animeId);
			
			
			for(Map<String,Object>map:result) {
				Parent parent = new Parent();
				
				parent.setId(((Number) map.get("id")).longValue());

				parentList.add(parent);
			}
			
			
    	}catch (Exception e) {
	 		Log.detail(Level.WARNING,"videotitleparenthashでエラー",e);
    	}
		return parentList;
		
	}

}

package com.example.web.etc.db.animeExclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ExcludeJDBC implements ExcludeDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public void insert(String title) {   
    	jdbc.update("INSERT INTO excludeAnime(title) VALUES(?)",title);
    }
    
	@Override
	public List<Exclude> selectAll() {
		try {
			String sql = "select title from excludeAnime order by id";
			List<Map<String, Object>> result = jdbc.queryForList(sql);
        
			List<Exclude> animeList = new ArrayList<>();
			for(Map<String,Object>map:result) {
				Exclude anime = new Exclude();
				
				anime.setTitle((String)map.get("title"));

				animeList.add(anime);
			}
			return animeList;
    	}catch (Exception e) {
	 		List<Exclude> animeList = new ArrayList<>();
	 		animeList.add(new Exclude());
	 		return animeList;
    	}
	}
	public void del() {
		jdbc.update("delete from excludeAnime");
	}
}

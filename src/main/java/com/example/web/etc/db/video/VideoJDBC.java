package com.example.web.etc.db.video;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VideoJDBC implements VideoDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
	public List<Video> selectOne(Integer id) {
		try {
			

			String sql = "select fname from video where anime_id =? ";
			List<Map<String, Object>> result = jdbc.queryForList(sql,id);
        
			List<Video> animeList = new ArrayList<>();
			for(Map<String,Object>map:result) {
				Video anime = new Video();			
				anime.setFname((String)map.get("fname"));
				animeList.add(anime);
			}
			return animeList;
    	}catch (Exception e) {
	 		List<Video> animeList = new ArrayList<>();
	 		animeList.add(new Video());
	 		return animeList;
    	}
	}

}

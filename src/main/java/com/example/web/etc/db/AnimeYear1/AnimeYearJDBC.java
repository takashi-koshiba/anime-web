package com.example.web.etc.db.AnimeYear1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AnimeYearJDBC implements AnimeYearDao {

    @Autowired
    private JdbcTemplate jdbc;

    


	@Override
	public List<AnimeYear> selectAll() {
		try {
			
		
			String sql = "select distinct year,season from ranked_anime_season "
					+ "					   where year <=("
					+ "					   SELECT"
					+ "					       YEAR(NOW()) AS currentYear"
					+ "					    )" 
					+ "and length(year)=4 and length(season) =1"

					+ "					    order by year  ,season  ";
;
			List<Map<String, Object>> result = jdbc.queryForList(sql);
        
			List<AnimeYear> animeList = new ArrayList<>();
			for(Map<String,Object>map:result) {
				AnimeYear anime = new AnimeYear();
        	
				anime.setYear((Integer)map.get("year"));
				anime.setSeason((Integer)map.get("season"));
				
        	
				animeList.add(anime);
			}
			return animeList;
    	}catch (Exception e) {
	 		List<AnimeYear> animeList = new ArrayList<>();
	 		animeList.add(new AnimeYear());
	 		return animeList;
    	}
	}
}

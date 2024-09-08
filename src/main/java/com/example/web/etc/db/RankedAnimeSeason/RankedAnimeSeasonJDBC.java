package com.example.web.etc.db.RankedAnimeSeason;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RankedAnimeSeasonJDBC implements RankedAnimeSeasonDao {

    @Autowired
    private JdbcTemplate jdbc;
/*
    @Override
    public int insert(Anime anime) {
        int row = jdbc.update("INSERT INTO anime(originalName, foldername) VALUES(?, ?)",
                anime.getOriginalName(), anime.getFoldername());
        return row;
    }


    @Override
    public int countRow(String text) {
        String sql = "SELECT count(*) as rownumber FROM anime WHERE originalName=? OR foldername=?";
        Object[] params = {text, text};
        List<Map<String, Object>> result = jdbc.queryForList(sql, params);

        // クエリ結果からカウント値を取得し、intにキャスト
        int count = 0;
        if (!result.isEmpty() && result.get(0).get("rownumber") != null) {
            count = ((Number) result.get(0).get("rownumber")).intValue();
        }

        System.out.print(count);
        
        return count;
    }

*/
    
    public List<RankedAnimeSeason>  selectAllQuerry(List<Map<String, Object>>  result) {
    	try {
    		
    	
    		List<RankedAnimeSeason> animeList = new ArrayList<>();
    		for(Map<String,Object>map:result) {
    			RankedAnimeSeason anime = new RankedAnimeSeason();
        	
    			anime.setAnime_id((Integer)map.get("anime_id"));
    			anime.setYear((Integer)map.get("year"));
    			anime.setSeason((Integer)map.get("season"));
    			anime.setAll_ranking((Integer)map.get("all_ranking"));
    			BigDecimal score = (BigDecimal) map.get("score");
    			anime.setScore( score);
    			anime.setOriginalName((String)map.get("originalName"));
    			anime.setFoldername((String)map.get("foldername"));
    			anime.setTxt((String)map.get("txt"));
    			anime.setRanking((BigInteger)map.get("ranking"));
        	
    			animeList.add(anime);
    		}
        
    		return animeList;
    	}catch (Exception e) {
        	
    		List<RankedAnimeSeason> animeList = new ArrayList<>();
    		animeList.add(new RankedAnimeSeason());
    		return animeList;
    	}
    }
    @Override
    public List<RankedAnimeSeason> selectAll(Integer year) {
    	try {
    		String sql = "select *, rank() over (order by score desc) as ranking " +
   	             "from ranked_anime_season " +
   	             "where year = ? " +
   	             "order by ranking";
   		
          List<Map<String, Object>> result = jdbc.queryForList(sql,year);
          
          return selectAllQuerry(result);
    	}catch (Exception e) {
        	
    		List<RankedAnimeSeason> animeList = new ArrayList<>();
    		animeList.add(new RankedAnimeSeason());
    		return animeList;
    	}

    }
    
    
    
	@Override
	public List<RankedAnimeSeason> selectAll(Integer year,Integer season) {
		try {
			String sql = "select *, rank() over (order by score desc) as ranking " +
	             "from ranked_anime_season " +
	             "where year = ? and season = ? " +
	             "order by ranking";
		
			List<Map<String, Object>> result = jdbc.queryForList(sql,year,season);
			return selectAllQuerry(result);
        
		}catch (Exception e) {
    	
			List<RankedAnimeSeason> animeList = new ArrayList<>();
			animeList.add(new RankedAnimeSeason());
			return animeList;
		}
    
	}
	
	@Override
	public List<RankedAnimeSeason> selectAll() {
		try {
			String sql = "select *, \n"
					+ "       rank() over (order by score desc) as ranking \n"
					+ "from ranked_anime_season \n"
					+ "where year = (select year \n"
					+ "              from (select year, season \n"
					+ "                    from ranked_anime_season \n"
					+ "                    group by year, season \n"
					+ "                    order by year desc, season desc \n"
					+ "                    limit 1) as y) \n"
					+ "and season = (select season \n"
					+ "              from (select year, season \n"
					+ "                    from ranked_anime_season \n"
					+ "                    group by year, season \n"
					+ "                    order by year desc, season desc \n"
					+ "                    limit 1) as s) \n"
					+ "order by ranking;";
			
			List<Map<String, Object>> result = jdbc.queryForList(sql);
			
			return selectAllQuerry(result);
        
		}catch (Exception e) {
			System.out.println(e);
			List<RankedAnimeSeason> animeList = new ArrayList<>();
			animeList.add(new RankedAnimeSeason());
			return animeList;
		}
    
	}
}

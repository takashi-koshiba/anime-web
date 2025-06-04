package com.example.web.etc.db.extendVideoRank;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ExtendVideoJDBC implements ExtendVideoDao {

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
    
    public List<ExtendVideo>  selectAllQuerry(List<Map<String, Object>>  result) {
    	try {
    		
    	
    		List<ExtendVideo> animeList = new ArrayList<>();
    		for(Map<String,Object>map:result) {
    			ExtendVideo anime = new ExtendVideo();
        	
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
        	
    		List<ExtendVideo> animeList = new ArrayList<>();
    		animeList.add(new ExtendVideo());
    		return animeList;
    	}
    }
    @Override
    public List<ExtendVideo> selectAll(Integer year) {
    	try {
    		String sql = "select *, rank() over (order by score desc) as ranking "
    				+ "from ranked_anime_season\n"
    				
    				+ "where year = ?  and anime_id in  (select anime_id from video_info  where video_time > '00:50:00') "

    				+ "order by ranking";
   		
          List<Map<String, Object>> result = jdbc.queryForList(sql,year);
          
          return selectAllQuerry(result);
    	}catch (Exception e) {
        	
    		List<ExtendVideo> animeList = new ArrayList<>();
    		animeList.add(new ExtendVideo());
    		return animeList;
    	}

    }
    
    
    
	@Override
	public List<ExtendVideo> selectAll(Integer year,Integer season) {
		try {
			System.out.println(year);
			System.out.println(season);
			String sql = "select *, rank() over (order by score desc) as ranking "
    				+ "from ranked_anime_season "
    				

    				+ "where year = ? and season= ? "
    				+"and anime_id in  (select anime_id from video_info  where video_time > '00:50:00') "
    				

    				
    				+ "order by ranking";
		
			List<Map<String, Object>> result = jdbc.queryForList(sql,year,season);
			return selectAllQuerry(result);
        
		}catch (Exception e) {
			System.out.println(e);
			List<ExtendVideo> animeList = new ArrayList<>();
			animeList.add(new ExtendVideo());
			return animeList;
		}
    
	}
	
	@Override
	public List<ExtendVideo> selectAll() {
		try {
			String sql = "select *, rank() over (order by score desc) as ranking "
    				+ "from ranked_anime_season "
    				

    				+"where anime_id in  (select anime_id from video_info  where video_time > '00:50:00') "
					+ " and  (year,season) =("
					+ "select year as max_year,season as max_season from(\n"
					+ "  select max(year) as year,season from ranked_anime_season \n"
					+ "  where year<=YEAR(now())\n"
					+ "  group by season\n"
					+ "  order by max(year) desc ,season desc\n"
					+ "  limit 1\n"
					+ ") as maxSeason)"
    				
    				+ "order by ranking";
			
			List<Map<String, Object>> result = jdbc.queryForList(sql);
			
			return selectAllQuerry(result);
        
		}catch (Exception e) {
			System.out.println(e);
			List<ExtendVideo> animeList = new ArrayList<>();
			animeList.add(new ExtendVideo());
			return animeList;
		}
    
	}
}

package com.example.web.etc.db.rankedAnime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RankedAnimeJDBC implements RankdAnimeDao {

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
    @Override
    public RankedAnime selectOne(Integer animeId) {
    	
    	try {
    		String sql = "SELECT anime_id, originalName, foldername, score, txt, ranking " +
                    "FROM ranked_anime " +
                    "WHERE anime_id = ? " +
                    "ORDER BY ranking";
            Map<String, Object> result = jdbc.queryForMap(sql,animeId);

            	RankedAnime anime = new RankedAnime();
            	
            	anime.setAnime_id((Integer)result.get("Anime_id"));
            	anime.setFoldername((String)result.get("foldername"));
            	anime.setOriginalName((String)result.get("originalName"));
            	
            	anime.setScore((BigDecimal)result.get("score"));
            	anime.setTxt((String)result.get("txt"));
            	anime.setRanking((Integer)result.get("ranking"));
            	return anime;
    	}catch (Exception e) {
    	
            return new RankedAnime();
        }
    	
    	

        
		
    }
    
	@Override
	public List<RankedAnime> selectAll() {
	 	try {
	 		String sql = "SELECT anime_id,originalName,foldername,score,txt,ranking from ranked_anime order by ranking ";
	 		List<Map<String, Object>> result = jdbc.queryForList(sql);
        
	 		List<RankedAnime> animeList = new ArrayList<>();
	 		for(Map<String,Object>map:result) {
	 			RankedAnime anime = new RankedAnime();
        	
	 			anime.setAnime_id((Integer)map.get("Anime_id"));
	 			anime.setFoldername((String)map.get("foldername"));
	 			anime.setOriginalName((String)map.get("originalName"));
	 			
	 			anime.setScore((BigDecimal)map.get("score"));
	 			anime.setTxt((String)map.get("txt"));
	 			anime.setRanking((Integer)map.get("ranking"));
        	
	 			animeList.add(anime);
	 		}
	 		return animeList;
	 	
	 	}catch (Exception e) {
	 		List<RankedAnime> animeList = new ArrayList<>();
	 		animeList.add(new RankedAnime());
	 		return animeList;
	 	}
	}
}

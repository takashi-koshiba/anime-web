package com.example.web.etc.db.Animetable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AnimeJDBC implements AnimeDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public int insert(Anime anime) {
        int row = jdbc.update("INSERT INTO anime(originalName, foldername) VALUES(?, ?)",
                anime.getOriginalName(), anime.getFoldername());
        return row;
    }


    @Override
    public int countRow(String text) {
    	try {
    		
    	
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
    	}catch (Exception e) {
        	return 0;
    	}
    }
    @Override
	public List<Anime> selectOne(Integer id) {
		try {
			
		
			String sql = "SELECT id,originalName,foldername from anime where id =? ";
			List<Map<String, Object>> result = jdbc.queryForList(sql,id);
        
			List<Anime> animeList = new ArrayList<>();
			for(Map<String,Object>map:result) {
				Anime anime = new Anime();
        	
				anime.setId((Integer)map.get("id"));
				anime.setFoldername((String)map.get("foldername"));
				anime.setOriginalName((String)map.get("originalName"));
        	
				animeList.add(anime);
			}
			return animeList;
    	}catch (Exception e) {
	 		List<Anime> animeList = new ArrayList<>();
	 		animeList.add(new Anime());
	 		return animeList;
    	}
	}

	@Override
	public List<Anime> selectAll() {
		try {
			
		
			String sql = "SELECT id,originalName,foldername from anime  order by  CHAR_LENGTH(foldername) desc ";
			List<Map<String, Object>> result = jdbc.queryForList(sql);
        
			List<Anime> animeList = new ArrayList<>();
			for(Map<String,Object>map:result) {
				Anime anime = new Anime();
        	
				anime.setId((Integer)map.get("id"));
				anime.setFoldername((String)map.get("foldername"));
				anime.setOriginalName((String)map.get("originalName"));
        	
				animeList.add(anime);
			}
			return animeList;
    	}catch (Exception e) {
	 		List<Anime> animeList = new ArrayList<>();
	 		animeList.add(new Anime());
	 		return animeList;
    	}
	}
	
	@Override
	public List<prefix> selectPrefixAll(){
		try {
			String sql = "select id,txt from prefix order by id";
			List<Map<String, Object>> result = jdbc.queryForList(sql);
        
			List<prefix> animeList = new ArrayList<>();
			for(Map<String,Object>map:result) {
				prefix anime = new prefix();
        	
				anime.setId((Integer)map.get("id"));
				anime.setTxt((String)map.get("txt"));
			
				animeList.add(anime);
			}
			return animeList;
    	}catch (Exception e) {
	 		List<prefix> animeList = new ArrayList<>();
	 		animeList.add(new prefix());
	 		return animeList;
    	}
		
	}
	
	@Override
	public List<AnimeSort> selectAllSortByText(Integer charId) {
		try {
			String sql;
			List<Map<String, Object>> result;
			if(charId==-1) {
				sql = "select t.id,t.originalName,t.foldername,txtChar,coalesce(prefix.id,0) as charId,\n"
						+ "						CASE \n"
						+ "						    WHEN coalesce(prefix.id,0) = 0 then 1  -- NULLの場合は一番下に\n"
						+ "						    ELSE 0 \n"
						+ "						    END 'sort' , coalesce(score,0) as score \n"
						+ "						from (\n"
						+ "						     SELECT id,originalName,foldername,substr(foldername,1,1) as txtChar\n"
						+ "						     from anime\n"
						+ "						     )as t\n"
						+ "						left join prefix on t.txtChar =prefix.txt\n"
						+ "left join ranked_anime on t.id=ranked_anime.anime_id\n"
						+ "						order by sort,charId,foldername";
				result = jdbc.queryForList(sql);
			}else {
				sql = "select t.id,t.originalName,t.foldername,txtChar,coalesce(prefix.id,0) as charId,\n"
						+ "						CASE \n"
						+ "						    WHEN coalesce(prefix.id,0) = 0 then 1  -- NULLの場合は一番下に\n"
						+ "						    ELSE 0 \n"
						+ "						    END 'sort' , coalesce(score,0) as score \n"
						+ "						from (\n"
						+ "						     SELECT id,originalName,foldername,substr(foldername,1,1) as txtChar\n"
						+ "						     from anime\n"
						+ "						     )as t\n"
						+ "						left join prefix on t.txtChar =prefix.txt\n"
						+ "left join ranked_anime on t.id=ranked_anime.anime_id\n"
						+ "where coalesce(prefix.id,0)=?\n"
						+ "						order by sort,charId,foldername";
				result = jdbc.queryForList(sql,charId);
			}
			
			
			
			
			List<AnimeSort> animeList = new ArrayList<>();
			
			for(Map<String,Object>map:result) {
				AnimeSort anime = new AnimeSort();
				anime.setId((Integer)map.get("id"));
				anime.setFoldername((String)map.get("foldername"));
				anime.setOriginalName((String)map.get("originalName"));
				anime.setTxtChar((String)map.get("txtChar"));
				anime.setCharId((Long)map.get("charId"));
				anime.setSort((Integer)map.get("sort"));
				anime.setScore((BigDecimal)map.get("score"));
				animeList.add(anime);

				
				
			}
			
			return animeList;
    	}catch (Exception e) {
    		System.out.println(e);
	 		List<AnimeSort> animeList = new ArrayList<>();
	 		animeList.add(new AnimeSort());
	 		return animeList;
    	}
	}
}

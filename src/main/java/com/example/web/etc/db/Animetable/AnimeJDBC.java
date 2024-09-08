package com.example.web.etc.db.Animetable;

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
	public List<Anime> selectAll() {
		try {
			
		
			String sql = "SELECT id,originalName,foldername from anime order by  CHAR_LENGTH(foldername) desc ";
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
}

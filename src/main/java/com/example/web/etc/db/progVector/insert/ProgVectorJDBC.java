package com.example.web.etc.db.progVector.insert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.web.etc.db.progVector.select.ProgStrVector;
import com.example.web.etc.sta.Kakasi;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.TextRep;

@Repository
public class ProgVectorJDBC implements ProgVectorDao {

    @Autowired
    private JdbcTemplate jdbc;
    
    @Autowired
    ProgStrVector strVector; 
    int tableId=2;
    @Override
	public void insertTitle(int limitter) {
    	
    	
    	Log.log(Level.INFO, "ベクトル書き込みスタート");

    	
    	List<ProgVector> progList = selectAll(limitter);
    	int j=0;
    	
    	for(ProgVector v:progList) {
    		Log.log(Level.INFO, "残り："+(progList.size()-j));
    		
    		Long id = strVector.insertStrVecParent(tableId, v.getProgId(),v.getVideoId());
    		String[] lines = v.getProg().split("\\R");
    		
    		int i=0;
    		for (String line :lines) {
    			String[] strs=txtTrans(line);
    			for(String s:strs) {
    				strVector.InsertTxt(s, i,id);
    				i++;
    			}
    			
    			
    		}
    		j++;
    	}

    	Log.log(Level.INFO, "ベクトル書き込み終了");

    }
    
    private String[] txtTrans(String str) {

        Set<String> tempSet = new LinkedHashSet<>();

        tempSet.add(str);
        tempSet.add(Kakasi.main(TextRep.main(str, true), "-JH -KH"));
        tempSet.add(Kakasi.main(TextRep.main(str, true), "-KH "));

        return tempSet.toArray(new String[0]);
    }
    @Override
    public Long countSelect() {
    	String sql ="select count(*) as c from video_prog join video using (video_id) where (anime_id,video_id) not  in "
    			+ "( "
    			+ "select parentId,childId from strvecparent where tableId = ?     and parentId is not null and childId is not null "
    			+ ")" ;
    	
    	List<Map<String, Object>> result = jdbc.queryForList(sql,tableId);
    	List<ProgVector> animeList = new ArrayList<>();
    	
    	Long c = 0L;
		for(Map<String,Object>map:result) {
			ProgVector anime = new ProgVector();
			c=(((Number) map.get("c")).longValue());


		}
		return c;
    }
    
    private List<ProgVector> selectAll(int limitter){
    	String sql ="select anime_id, video_id,txt from video_prog join video using (video_id) where (anime_id,video_id) not  in "
    			+ "( "
    			+ "select parentId,childId from strvecparent where tableId = ?     and parentId is not null and childId is not null "
    			+ ") limit "+limitter;
    	
    	List<Map<String, Object>> result = jdbc.queryForList(sql,tableId);
    	List<ProgVector> animeList = new ArrayList<>();
		for(Map<String,Object>map:result) {
			ProgVector anime = new ProgVector();
			anime.setVideoId(((Number) map.get("video_id")).longValue());

			anime.setProgId((Integer)map.get("anime_id"));
			anime.setProg((String)map.get("txt"));
    	
			animeList.add(anime);
		}
		
		return animeList;
    	
    }
    
    
}

package com.example.web.etc.db.animeVector.insert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.hibernate.query.IllegalQueryOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.web.etc.db.animeVector.select.AnimeStrVector;
import com.example.web.etc.sta.Kakasi;
import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.TextRep;

@Repository
public class AnimeInsertJDBC implements AnimeInsertrDao {

    @Autowired
    private JdbcTemplate jdbc;
    
    @Autowired
    AnimeStrVector strVector; 
    int tableId=1;
    @Override
	public void insertTitle(int limitter) {
    	
    	
    	Log.log(Level.INFO, "ベクトル書き込みスタート");

    	
    	List<AnimeInsert> progList = selectAll(tableId,limitter);
    	int j=0;
    	
    	for(AnimeInsert v:progList) {
    		Log.log(Level.INFO, "残り："+(progList.size()-j));
    		
    		Long id = strVector.insertStrVecParent(tableId, v.getAnimeid(),v.getChildId());
    		String[] lines = v.getProg().split("\\R");
    		for (int i=0;i<lines.length;i++) {
    		
    			strVector.InsertTxt(lines[i], i,id);
    		}
    		j++;
    	}

    	Log.log(Level.INFO, "ベクトル書き込み終了");

    }
   
    @Override
    public void delAnimeVector(int parentId) {
    	try {
    		String sql = "delete from strvecparent where parentId=? and tableId=? ";
        	jdbc.update(sql,parentId,tableId);
        	Log.log(Level.INFO,parentId+"をstrvecparentから削除しました。");
    	}catch (Exception e) {
			Log.detail(Level.WARNING, "strvecparentの削除でエラー", e);
		}
    	
    	
    	

    }
    @Override
    public int showCount() {
      	String sql = "select count(*) as c from ( "
    			+ "select id,txt from ( "
    			+ "    select id,originalName as txt from anime "
    			+ ")as t1 "
    			+ "union select id,foldername as txt from anime "
    			+ "union select anime_id as id,fname as txt from alias "
    			+ ") as t1 "
    			+ "where (id) not in (select parentId from strvecparent where tableId = ?     and parentId is not null and childId is not null) "
    			
    			;
      	List<Map<String, Object>> result = jdbc.queryForList(sql,tableId);
      	for(Map<String,Object>map:result) {
			
			int id =((Number) map.get("c")).intValue();
			return id;
      	}
    	throw new IllegalQueryOperationException(sql);
		
    	
    }
    
    //アニメのタイトルや別名を取得
    private List<AnimeInsert> selectAll(int tableId,int limitter){
    	
    	List<Object> arary = new ArrayList<>();
    	
    	String sql = "select id,txt from ( "
    			+ "select id,txt from ( "
    			+ "    select id,originalName as txt from anime "
    			+ ")as t1 "
    			+ "union select id,foldername as txt from anime "
    			+ "union select anime_id as id ,fname as txt from alias "
    			+ ") as t1 "
    			+ "where (id) not in (select parentId from strvecparent where tableId = ?     and parentId is not null and childId is not null) "
    			
    			;
    	arary.add(tableId);
    	if (limitter>0) {
    		sql+="and id in (select id from (select id from anime union select anime_id as id from alias)as t "
    				+ "where (id) not in (select parentId from strvecparent where tableId = ?     and parentId is not null and childId is not null)  ) limit "+limitter;
    		arary.add(tableId);
    	}
    

    	
    	List<Map<String, Object>> result = jdbc.queryForList(sql,arary.toArray());
    	List<AnimeInsert> animeList = new ArrayList<>();
		for(Map<String,Object>map:result) {
			
			int id =((Number) map.get("id")).intValue();
			String str=(String)map.get("txt");
			String[] strs=txtTrans(str);
			int childid=1;
			for(String s:strs) {
				AnimeInsert anime = new AnimeInsert();
				anime.setAnimeid(id);

				anime.setChildId(((Number) childid).longValue());
				anime.setProg(s);
	    	
				animeList.add(anime);
				childid++;
			}
			
			

		}
		
		return animeList;
    	
    }
    private String[] txtTrans(String str) {

        Set<String> tempSet = new LinkedHashSet<>();

        tempSet.add(str);
        tempSet.add(Kakasi.main(TextRep.main(str, true), "-JH -KH"));
        tempSet.add(Kakasi.main(TextRep.main(str, true), "-KH "));

        return tempSet.toArray(new String[0]);
    }

    
    
}

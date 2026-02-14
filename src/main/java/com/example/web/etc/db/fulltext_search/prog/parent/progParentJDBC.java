package com.example.web.etc.db.fulltext_search.prog.parent;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.web.etc.sta.Log;

@Repository
public class progParentJDBC implements progParentDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<video_prog> selectUnhashedParentId(boolean isLimit){
    	List<video_prog> video_progs = new ArrayList<>();
		try {
			String sql = "select video_prog.video_id,video_prog.txt "
					+ "from video_prog "
					+ "where  video_id not in "
					+ "(select video_id from progparent where video_id is not null ) ";
					if(isLimit) {
						sql+="limit 1";
					}
				
				
			List<Map<String, Object>> result = jdbc.queryForList(sql);
			//System.out.println(result);
			
			for(Map<String,Object>map:result) {
				video_prog video = new video_prog();
				
				video.setId(((Number) map.get("video_id")).longValue());
				video.setTxt(map.get("txt").toString());
				video_progs.add(video);
			}
			
			
    	}catch (Exception e) {
	 		Log.detail(Level.WARNING,"selectUnhashedParentIdでエラー",e);
	 		throw e;
	 		
    	}
		return video_progs;
    }
    
    @Override
    public long insert(long video_id ) {   

        KeyHolder keyHolder = new GeneratedKeyHolder();

    	jdbc.update(con -> {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO progparent(video_id) VALUES(?)",
            Statement.RETURN_GENERATED_KEYS
        );
        ps.setLong(1, video_id);
        return ps;
    	}, keyHolder);
    	

    return keyHolder.getKey().longValue();
    }
    
    @Override
    public void delete(int animeid) {
    	 jdbc.update("delete from proghash where anime_id =? ",animeid);

    }
    
	@Override
	public List<progParent> selectById(int animeId ) {
		List<progParent> parentList = new ArrayList<>();
		try {
			String sql = "select id from proghash where anime_id= ?";
			
			List<Map<String, Object>> result = jdbc.queryForList(sql,animeId);
			
			
			for(Map<String,Object>map:result) {
				progParent parent = new progParent();
				
				parent.setId(((Number) map.get("id")).longValue());

				parentList.add(parent);
			}
			
			
    	}catch (Exception e) {
	 		Log.detail(Level.WARNING,"proghashでエラー",e);
	 		throw e;
	 		
    	}
		return parentList;
		
	}

}

package com.example.web.etc.db.animeVector.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.web.etc.db.vector.VecDBJDBCAbstract;
import com.example.web.etc.sta.Log;

@Repository
public class AnimeSearchJDBC extends VecDBJDBCAbstract<AnimeVecDB> {

    @Autowired
    private JdbcTemplate jdbc;

    public List<AnimeVecDB> selectMachedStr(long[] avgNgrams, int[] inputCost, int tableId,int limit ) {
        List<AnimeVecDB> resultList = new ArrayList<>();

        try {
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder();

            
            
            sql.append("select strvecparent.parentId, ");
            
            	sql.append("min(ABS((strvec.cost*(strvec.cost+1)/2)-(t1.matchCount*(t1.matchCount+1)/2))) as diff, ");
            	sql.append("anime.originalname,anime.foldername,max(matchCount) as matchCount from strvec ");
            sql.append("join ( ");
            
            	sql.append("select linevecs.strvecid,count(*) as matchCount ");
            	sql.append("from linevecs ");
            	sql.append("join ( ");
            	for (int i = 0; i < avgNgrams.length; i++) {
            		if (i != 0) sql.append(" union all ");
            		sql.append("select id from wordvec where cost=? and vecAvg=? ");

            		params.add(inputCost[i]);
            		params.add(avgNgrams[i]);
            	}
            	sql.append(") w on w.id = linevecs.vecId ");
            	sql.append("group by linevecs.strvecid ");
            sql.append(")as t1 ");
            sql.append("on strvec.id=t1.strvecid ");
            sql.append("join strvecparent on strvecparent.id=strvec.vecparent_id ");
            sql.append("join anime on strvecparent.parentId=anime.id ");
            sql.append("where strvecparent.tableid=? ");
            params.add(tableId);
            
            sql.append("group by strvecparent.parentId ");
            sql.append("order by matchCount desc ,diff ");
            
            if(limit!=-1)sql.append("limit "+limit);
            
            

            List<Map<String, Object>> result = jdbc.queryForList(sql.toString(), params.toArray());

            for (Map<String, Object> map : result) {
                Long vecParent_id = ((Number) map.get("parentId")).longValue();
                Integer maxMatched = ((Number) map.get("matchCount")).intValue();
                double maxLineDiff = ((Number) map.get("diff")).doubleValue();
                String originalName = (String)map.get("originalName");
                String foldername = (String)map.get("foldername");

                resultList.add(new AnimeVecDB(vecParent_id, maxMatched, maxLineDiff, -1, 0, 1, originalName, foldername));
            
            }

            if (result.isEmpty()) {
                resultList.add(new AnimeVecDB(null, 0, 0, -1, 0, 1, "", ""));
            }
            
        } catch (Exception e) {
            Log.detail(Level.WARNING, "SQLが失敗しました。", e);
            throw e;
        }

        
        return resultList;
    }

	
}

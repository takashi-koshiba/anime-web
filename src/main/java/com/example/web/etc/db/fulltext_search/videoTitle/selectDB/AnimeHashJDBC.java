package com.example.web.etc.db.fulltext_search.videoTitle.selectDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.web.etc.sta.Log;
import com.example.web.etc.sta.NgramHasher;

@Repository
public class AnimeHashJDBC implements AnimeHashDao {

    @Autowired
    private JdbcTemplate jdbc;
    int lenNgram=2;
    
    public  List<AnimeHash> selectByHash(String str) {
       
        
        
        long[] hashes= toHashes(str);
		int countHash=hashes.length;

        
        List<AnimeHash> resultList = new ArrayList<>();
        try {
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder();

            
            sql.append("WITH t AS ( ");
            sql.append("  SELECT v.doc_id, ");
            sql.append("          v.gram_pos, ");
            sql.append("v.gram_pos - (ROW_NUMBER() OVER (PARTITION BY v.doc_id ORDER BY gram_pos) - 1) AS grp ");
            sql.append("FROM videotitlehash as v ");
            sql.append("WHERE v.hash_int IN ( ");
            for(int i =0;i<countHash;i++) {
            	if(i!=0) sql.append(",");
            	
            	sql.append("?");
            	params.add(hashes[i]);
            	
            }
            sql.append(") ");
            sql.append(") ");
            sql.append("select anime.id,max(t3.score) as score ,anime.originalName,anime.foldername from ( "
            		+ "    select t2.doc_id,sum(t2.streak_len*(t2.streak_len+1)/2) as score,doc.parent_id from ( "
            		+ "        SELECT t.doc_id, "
            		
            		+ "       COUNT(*) AS streak_len "
            		+ "        FROM t "
            		+ "        GROUP BY doc_id, grp "
            		+ "    ) as t2 "
            		+ "    join videotitledoc as doc on doc.id=t2.doc_id "
            		+ "    group by doc_id "
            		+ "    ORDER BY doc_id "
            		+ ") as t3 "
            		+ "join videotitleparenthash as parent on t3.parent_id=parent.id "
            		+ "join anime on parent.anime_id=anime.id "
            		+ "GROUP BY anime.id, anime.originalName, anime.foldername "
            			
            		+ "order by score desc limit 10");
            
           // if(limit!=-1)sql.append("limit "+limit);
            
            
            List<Map<String, Object>> result = jdbc.queryForList(sql.toString(), params.toArray());
            
            for (Map<String, Object> map : result) {
            	AnimeHash animeHash = new AnimeHash();
            	animeHash.setAnimeId(((Number) map.get("id")).intValue());
                animeHash.setScore(((Number) map.get("score")).floatValue());
            	animeHash.setOriginalName((String) map.get("originalName"));
            	animeHash.setFoldername((String)map.get("foldername"));

                resultList.add(animeHash);
            
            }

            
        } catch (Exception e) {
            Log.detail(Level.WARNING, "SQLが失敗しました。", e);
           
            throw e;
        }

        
        return resultList;
    }
    
    
    private long[] toHashes(String str) {

        String[] strs = str.trim().split("\\s+|　+");

        // まず概算サイズを計算
        int estimated = 0;
        for (String s : strs) {
            estimated += Math.max(0, s.length() - lenNgram + 1);
        }

        long[] hashes = new long[estimated];
        int index = 0;

        for (String s : strs) {
            String[] ngramStr = NgramHasher.strToArr(s, lenNgram);

            for (String n : ngramStr) {
                hashes[index++] = NgramHasher.strToVecNgram1D(n, lenNgram);
            }
        }

        return hashes;
    }


}

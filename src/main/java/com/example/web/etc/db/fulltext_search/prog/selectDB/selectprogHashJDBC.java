package com.example.web.etc.db.fulltext_search.prog.selectDB;

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
public class selectprogHashJDBC implements selectprogHashDao {

    @Autowired
    private JdbcTemplate jdbc;
    int lenNgram=2;
    

    public  List<selectprogHash> selectByHash(String str) {
       
        
        
        long[] hashes= toHashes(str);
		int countHash=hashes.length;

        
        List<selectprogHash> resultList = new ArrayList<>();
        try {
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder();

            
            sql.append("WITH t AS ( ");
            sql.append("  SELECT v.doc_id, ");
            sql.append("          v.gram_pos, ");
            sql.append("v.gram_pos - (ROW_NUMBER() OVER (PARTITION BY v.doc_id ORDER BY gram_pos) - 1) AS grp ");
            sql.append("FROM proghash as v ");
            sql.append("WHERE v.hash_int IN ( ");
            for(int i =0;i<countHash;i++) {
            	if(i!=0) sql.append(",");
            	
            	sql.append("?");
            	params.add(hashes[i]);
            	
            }
            sql.append(") ");
            sql.append(") ");
            sql.append("select t4.anime_id,t4.score,anime.originalName,anime.foldername from( "
            		+ "    select t3.anime_id,max(t3.score) as score from ( "
            		+ "        select t2.doc_id, "
            		
            		+ "       sum(t2.len*(t2.len+1)/2) as score, "
            		+ "        doc.parent_id,parent.video_id ,video.anime_id "
            		+ "        from ( "
            		+ "            select t.doc_id,count(*) as len "
            		+ "            from t "
            		+ "            group by t.doc_id,t.grp "
            		+ "        ) as t2 "
            		+ "        join progdoc as doc on doc.id=t2.doc_id "
            		+ "        join progparent as parent on parent.id=doc.parent_id "
            		+ "        join video on parent.video_id=video.video_id "
            	    + "        group by t2.doc_id "
            		+"         )as t3 "
            	    +"         group by anime_id "
            		+"         ) as t4 "
            	    
            		+ "join anime on anime.id=t4.anime_id "
            		+"order by t4.score desc limit 10 ");
            
           // if(limit!=-1)sql.append("limit "+limit);
            
            
            List<Map<String, Object>> result = jdbc.queryForList(sql.toString(), params.toArray());
            
            for (Map<String, Object> map : result) {
            	selectprogHash progHash = new selectprogHash();
            	progHash.setAnimeId(((Number) map.get("anime_id")).intValue());
            	progHash.setScore(((Number) map.get("score")).floatValue());
            	progHash.setOriginalName((String) map.get("originalName"));
            	progHash.setFoldername((String)map.get("foldername"));

    
                resultList.add(progHash);
            
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
            	//System.out.println(NgramHasher.strToVecNgram1D(n, lenNgram));
                hashes[index++] = NgramHasher.strToVecNgram1D(n, lenNgram);
                
            }
            
        }
        
        return hashes;
    }


}

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

    public List<AnimeVecDB> selectMachedStr(long[] avgNgrams, int[] inputCost, int tableId) {
        List<AnimeVecDB> resultList = new ArrayList<>();

        try {
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder();

            sql.append("select ABS(rowCost-diff)/rowCost as matchRatio, rowCost, parentId, strVecId, rowNumber, countRow, wordCost,originalName,foldername ");
            sql.append("from ( ");
            sql.append("select row_number() over(PARTITION BY parentId order by count(*)  desc) idrank, ");
            sql.append("min(ABS(strvec.cost-").append(avgNgrams.length).append(")) as diff, ");
            sql.append("max(strvec.cost) as rowCost, parentId, strVecId, rownumber, count(*) as countRow, max(t1.cost) as wordCost ");
            sql.append("from linevecs ");
            sql.append("join ( ");
            sql.append("select id, cost from wordvec where ");

            for (int i = 0; i < avgNgrams.length; i++) {
                if (i != 0) sql.append(" or ");
                sql.append("(cost=? and vecAvg=?) ");
                params.add(inputCost[i]);
                params.add(avgNgrams[i]);
            }

            sql.append(") as t1 on linevecs.vecId = t1.id ");
            sql.append("join strvec on linevecs.strVecId = strvec.id ");
            sql.append("join strvecparent on strvec.vecParent_id = strvecparent.id ");
            sql.append("where tableId = ? ");
            sql.append("group by parentId, strVecId, rownumber ");
            sql.append(") as t1 ");
            sql.append("join anime on t1 .parentId=anime.id ");
            sql.append("where idrank = 1 ");
            params.add(tableId);
            sql.append("order by countRow desc, matchRatio");

            List<Map<String, Object>> result = jdbc.queryForList(sql.toString(), params.toArray());

            for (Map<String, Object> map : result) {
                Long vecParent_id = ((Number) map.get("parentId")).longValue();
                int maxMatched = ((Number) map.get("countRow")).intValue();

                double maxLineDiff = ((Number) map.get("matchRatio")).doubleValue();
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

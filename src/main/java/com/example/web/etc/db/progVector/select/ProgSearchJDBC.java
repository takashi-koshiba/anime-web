package com.example.web.etc.db.progVector.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.web.etc.db.vector.VecDB;
import com.example.web.etc.db.vector.VecDBJDBCAbstract;
import com.example.web.etc.sta.Log;

@Repository
public class ProgSearchJDBC extends VecDBJDBCAbstract {
    @Autowired
    private JdbcTemplate jdbc;
    
    
	@Override
	public  List<VecDB> selectMachedStr(long[] avgNgrams,int[] inputCost,int tableId,int limit){
    	List<VecDB> resultList = new ArrayList<>();
    	
    	
    	try {
    		List<Object> params = new ArrayList<>();
    		StringBuilder sql = new StringBuilder();
    		sql.append("select ABS(rowCost-diff)/rowCost as matchRatio, rowCost,parentId,strVecId,rowNumber,countRow,wordCost ");
    		sql.append("from ( ");
    			sql.append("select  row_number() over(PARTITION BY parentId order by  min(ABS(strvec.cost-t1.cost)) asc) idrank, ");
    			sql.append("min(ABS(strvec.cost-"+avgNgrams.length+")) as diff,max(strvec.cost) as rowCost, ");
    			sql.append("parentId,strVecId,rownumber,count(*) as countRow,max(t1.cost) as wordCost from linevecs ");
    			sql.append("join ( ");
    				sql.append("select id,cost from wordvec where  ");
    				
    				int i=0;
    				for(long avgNgram:avgNgrams) {
    					if (i!=0) sql.append(" or ");
    					sql.append(" (cost=? and vecAvg=?) ");
    					params.add(inputCost[i]);
    					params.add(avgNgram);
    					

    					
    					
    					i++;
    					
    				}
    		
    			sql.append(") as t1	on linevecs.vecId=t1.id ");
    			sql.append("join strvec on linevecs.strVecId=strvec.id ");
    			sql.append("join strvecparent on strvec.vecParent_id=strvecparent.id ");
    			sql.append("where tableId=? ");
    			sql.append("group by parentId,strVecId,rownumber ");
    			sql.append(") as t1  ");
    		sql.append("where idrank= 1 ");
    		params.add(tableId);
    		
    		sql.append("order by countRow desc ,matchRatio  ");
    	//	List<BigInteger> resultVecParentId= selectVecParentByVec(avgNgrams, ngrams);

    		//BigInteger[] VecParentIdArray = resultVecParentId.toArray(new BigInteger[0]);
    		
    		
            List<Map<String, Object>> result = jdbc.queryForList(sql.toString(), params.toArray());
            
            
			for(Map<String,Object>map:result) {
				
				Long vecParent_id = Long.valueOf(((Number) map.get("parentId")).longValue());
				int maxMatched = ((Number) map.get("countRow")).intValue();
				double maxLineDiff = ((Number) map.get("matchRatio")).doubleValue();

				resultList.add(new VecDB(vecParent_id,maxMatched,maxLineDiff,-1));
			}
			if (result.size()<1) {
				resultList.add(new VecDB(null,0,0,-1));
			}
			
			
    	}catch (Exception e) {

           
            Log.detail(Level.WARNING, "SQLが失敗しました。", e);
            throw e;
        }

        return resultList;
	    
    }


	
    
    
}

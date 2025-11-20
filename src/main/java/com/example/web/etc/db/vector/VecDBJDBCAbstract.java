package com.example.web.etc.db.vector;

import java.math.BigInteger;
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
public abstract class VecDBJDBCAbstract<T extends VecDB> implements VecDBDao<T> {

    @Autowired
    private JdbcTemplate jdbc;

    //文字列ベクトルの管理データを挿入
    @Override
    public Long insertwordVecParent(long avgNgram,Integer cost) {
    	
    	try {
    		

    		List<Long> matchedList= this.selectVecParentByVec(avgNgram,cost) ;
    		if (matchedList.size()>0 && matchedList.getFirst() !=null) {

    			return matchedList.getFirst();
    		}
    		
    		String sql = "INSERT INTO wordVec (cost, vecAvg) VALUES (?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, cost);
                ps.setDouble(2, avgNgram);
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key == null) {
                throw new IllegalStateException("自動生成されたIDが取得できませんでした。");
            }
            
            
            Long parentId = Long.valueOf(key.longValue());
            //parentIdを引き継いで書き込む
         //   insertwordVecs(parentId,ngram);
            
            return parentId;
    	}
    	catch (Exception e) {

            Log.detail(Level.WARNING, "ベクトルの書き込みに失敗しました。", e);
            throw e;
        }

        
        
        
    }
    
   
    
    //StrVecLineの管理用
    //text全文の管理用
    @Override
    public Long insertstrVecParent(int tableId, int parentId,Long childId) {
    	
    	try {
    		String insertSql = "INSERT IGNORE INTO strVecParent (tableId, parentId, childId) VALUES (?, ?, ?)";
    		jdbc.update(insertSql, tableId, parentId, childId); 

    		Long id;
    		if (childId == null) {
    		    String selectSql = "SELECT id FROM strVecParent WHERE tableId = ? AND parentId = ? AND childId IS NULL";
    		    id = jdbc.queryForObject(selectSql, Long.class, tableId, parentId);
    		} else {
    		    String selectSql = "SELECT id FROM strVecParent WHERE tableId = ? AND parentId = ? AND childId = ?";

    		    id = jdbc.queryForObject(selectSql, Long.class, tableId, parentId, childId);
    		}
    		return id;


    		
    		
            
    	}catch (Exception e) {

           
            Log.detail(Level.WARNING, "ベクトルの書き込みに失敗しました。", e);
            throw e;
        }

            
           
            
    }
    //ベクトルから文字列の一致率を計算
    @Override
    public abstract   List<T>  selectMachedStr(long[] avgNgrams,int[] inputCost,int tableId);
    
    /*{
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
    				for(double avgNgram:avgNgrams) {
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
	    
    }*/

    //strVecParentをキーにして複数行のテキストのIDを挿入
    //textの１行毎に管理
    @Override
    public void insertStrVecLine(Long strVecParentId,int rowNumber,int cost,List<Long> vecParent_id) {
    	try {
    		
    		String sql = "INSERT INTO strVec (vecParent_id, rowNumber,cost) VALUES (?, ?,?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            
            	jdbc.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, strVecParentId.longValue());
                    ps.setInt(2, rowNumber);
                    ps.setInt(3, cost);
                    return ps;
                }, keyHolder);

                Number key = keyHolder.getKey();
                if (key == null) {
                    throw new IllegalStateException("自動生成されたIDが取得できませんでした。");
                }
                BigInteger Id = BigInteger.valueOf(key.longValue());
            
                //parentIdを引き継いで書き込む
                insertLineVecs(Id,vecParent_id);
    	}catch (Exception e) {

           
            Log.detail(Level.WARNING, "ベクトルの書き込みに失敗しました。", e);
            throw e;
        }
    	
           
        
    }
    //strVecLineの１行に対するベクトルをここで追加
    //一行のテキストを分割したテキストのベクトルIDを挿入
    private void  insertLineVecs(BigInteger StrVecLineId,List<Long> vecIdList) {
    	try {
    	
            String sql = "INSERT INTO lineVecs (strVecId, vecId) VALUES (?, ?)";
            List<Object[]> batchArgs = new ArrayList<>();
            for (Long vecId : vecIdList) {
                batchArgs.add(new Object[]{StrVecLineId, vecId});
            }

            jdbc.batchUpdate(sql, batchArgs);
           
    	}catch (Exception e) {

           
            Log.detail(Level.WARNING, "ベクトルの書き込みに失敗しました。", e);
            throw e;
        }

    }
    
   
    public List<Long> selectVecParentByVec(long avgNgram,int inputcost) {

        return this.selectVecParentByVec(new long[] { avgNgram },new int[] {inputcost});
    }
    
    @Override
    public List<Long> selectVecParentByVec(long[] avgNgrams,int[] inputCost) {
        List<Long> resultList = new ArrayList<>();

        try {
        	List<Object> params = new ArrayList<>();

        	StringBuilder sql = new StringBuilder();
        	sql.append("select id from wordvec where ");
        	
			int i=0;
			for(long avgNgram:avgNgrams) {
				if (i!=0) sql.append(" or ");
				sql.append(" (cost=? and vecAvg=?) ");
				params.add(inputCost[i]);
				params.add(avgNgram);
				
				
				i++;
				
			}
        //	params.add(vecs[0].length);

            
           
            List<Map<String, Object>> result = jdbc.queryForList(sql.toString(), params.toArray());

        
			List<Long> list = new ArrayList<>();
			
			
			for(Map<String,Object>map:result) {

				list.add((Long)map.get("parent_id"));
			}
			if (list.size()<1) {
				list.add(null);
			}
			
			return list;
    	} catch (Exception e) {


            Log.detail(Level.WARNING, "queryが失敗しました。", e);
            // エラー時は空の要素を返す
            resultList.add(null);
            throw e;
        }

       // return resultList;
    }
    
}

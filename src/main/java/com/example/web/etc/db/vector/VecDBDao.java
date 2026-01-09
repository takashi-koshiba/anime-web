package com.example.web.etc.db.vector;

import java.util.List;

public interface VecDBDao<T> {
	public List<Long> selectVecParentByVec(long[] avgNgram,int inputCost[]);
	public List<Long> selectVecParentByVec(long avgNgram, int inputCost);
	
	public Long insertwordVecParent(long avgNgram,Integer cost);
	public void  insertStrVecLine(Long strVecParentId,int rowNumber,int cost,List<Long> vecParent_id)  ;
	//private void  insertLineVecs(BigInteger StrVecLineId,List<BigInteger> vecIdList) ;

	public Long insertstrVecParent(int tableId, int parentId,Long childId);
	public List<T> selectMachedStr(long[] avgNgrams,int[] inputCost,int tableId,int limit);
	
		
	}


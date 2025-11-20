package com.example.web.etc.db.vector;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public abstract class VecDBSearvice<T extends VecDB> {
	private final VecDBJDBCAbstract<T> animeJDBC; 

    public VecDBSearvice(VecDBJDBCAbstract<T> animeJDBC) {
        this.animeJDBC = animeJDBC;
    }

    public List<T> selectMachedStr(long[] avgNgrams, int[] inputCost, int tableId) {
        return animeJDBC.selectMachedStr(avgNgrams, inputCost, tableId);
    }

    public List<Long> selectVecParentByVec(long[] avgNgram, int[] inputCost) {
        return animeJDBC.selectVecParentByVec(avgNgram, inputCost);
    }

    public List<Long> selectVecParentByVec(long avgNgram, int inputCost) {
        return animeJDBC.selectVecParentByVec(avgNgram, inputCost);
    }

    public void insertStrVecLine(Long strVecParentId, int rowNumber, int cost, List<Long> vecParent_id) {
        animeJDBC.insertStrVecLine(strVecParentId, rowNumber, cost, vecParent_id);
    }

    public Long insertwordVecParent(long avgNgram, Integer cost) {
        return animeJDBC.insertwordVecParent(avgNgram, cost);
    }

    public Long insertstrVecParent(int tableId, int parentId, Long childId) {
        return animeJDBC.insertstrVecParent(tableId, parentId, childId);
    }

}

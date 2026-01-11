package com.example.web.etc.db.animeVector.select;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.web.etc.db.vector.VecDB;
import com.example.web.etc.sta.SimilarWards;
import com.example.web.etc.sta.StrVector;

@Component
public class AnimeStrVector extends StrVector<AnimeVecDB> {
	
	int maxLen=2;
    public AnimeStrVector(AnimeSelectSearvice vec) {
        super(vec);
    }

    @Override
    protected List<AnimeVecDB> calcMatchedStr(String str, int tableId,int limit) {
        String[] split = strToArr(str);

        int countSplit = split.length;
        long[] avgNgrams = new long[countSplit];
        int[] costs = new int[countSplit];

        for (int i = 0; i < countSplit; i++) {
            String s = split[i];
            long avgNgram = strToVecNgram1D(s, 2);
            avgNgrams[i] = avgNgram;
            costs[i] = SimilarWards.maxLength(s.length(), false,maxLen);
        }

        List<AnimeVecDB> result = vec.selectMachedStr(avgNgrams, costs, tableId,limit);
        return result;
    }

    @Override
    public List<VecDB> selectStr(String input, int tableId,int limit) {
        if (input.length() > 100)
            throw new IllegalArgumentException("入力文字が長すぎます: ");

       //long startTime = System.currentTimeMillis();

        
        String[] strs = input.trim().split("\\s+|　+");
        List<AnimeVecDB> all = new ArrayList<>();

        for (String str : strs) {
        	if(str.length()<2) continue;
        	
        	
            List<AnimeVecDB> result = calcMatchedStr(str, tableId,-1);
   
            all.addAll(result);
            
        }
        
       
        List<VecDB> sortedList = all.stream()
        	    .filter(Objects::nonNull)
        	    .filter(v -> v.getVecParent_id() != null)
        	    // グループ化して集約
        	    .collect(Collectors.groupingBy(
        	        AnimeVecDB::getVecParent_id,
        	        Collectors.reducing(
        	            null,
        	            v -> v,
        	            (a, b) -> {
        	                if (a == null) return b;

        	                int sumMatched = a.getMaxMatched() + b.getMaxMatched();
        	                double sumDiff = a.getMaxLineDiff() + b.getMaxLineDiff();

        	                return new AnimeVecDB(
        	                    a.getVecParent_id(),
        	                    sumMatched,
        	                    sumDiff, 
        	                    -1, 0, 1,
        	                    a.getOriginal(),
        	                    a.getTitle()
        	                );
        	            }
        	        )
        	    ))
        	    .values().stream() 
        	    .map(v -> {
        	        // 平均を計算
        	        double avgDiff = v.getMaxLineDiff() / strs.length;
        	        return new AnimeVecDB(
        	            v.getVecParent_id(),
        	            v.getMaxMatched(),
        	            avgDiff,
        	            -1, 0, 1,
        	            v.getOriginal(),
        	            v.getTitle()
        	        );
        	    })
        	   
        	    .sorted(Comparator
        	        .comparingInt(AnimeVecDB::getMaxMatched).reversed()
        	        .thenComparingDouble(AnimeVecDB::getMaxLineDiff)
        	    )
        	    .limit(limit)
        	    .map(v -> (VecDB)v) 
        	    .collect(Collectors.toList());

        	return sortedList;

    }
}

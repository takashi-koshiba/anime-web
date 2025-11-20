package com.example.web.etc.sta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.web.etc.db.vector.VecDB;
import com.example.web.etc.db.vector.VecDBSearvice;

@Component
public abstract class StrVector<T extends VecDB> {
	int maxLen=2;
	
	
	
	protected final VecDBSearvice vec;

    protected StrVector(VecDBSearvice vec) {
        this.vec = vec;
    }
	
	@Transactional
	protected Long insertToVec(long avgNgram,int cost) {
		

		
		return vec.insertwordVecParent(avgNgram, cost);
	}
	
	@Transactional
	public Long insertStrVecParent(int tableId,int parentId,Long childId) {
		Long id = vec.insertstrVecParent(tableId,parentId,childId);
		return id;
	}
	
	//テキストをDBに挿入
	@Transactional(rollbackFor = Exception.class)
	public void InsertTxt(String str ,int rows,Long StrvecParentId) {

		try {
			Log.log(Level.INFO, "ベクトルを書き込み中"+str);
			
			String split[] = strToArr(str) ;
			

			//分割した文字のベクトルIDを取得
			List<Long>  vecId = getWordVecs(split,true);
			
	
			int cost = SimilarWards.maxLength(str.length(), false,maxLen);

			
			//ベクトルIDとテキストをDBにいれて紐づける
			vec.insertStrVecLine(StrvecParentId, 0,cost, vecId);
			Log.log(Level.INFO, "ベクトル書き込み完了"+str);
			
			
		} catch (Exception e) {
            e.printStackTrace();
            Log.detail(Level.WARNING, str, e);
            throw e;
        }
		
	
		
	}
	@Transactional
	//なければベクトルを入れ、一致するvecIDを返す
	private List<Long> getWordVecs(String[] split,boolean canInsert) {
	    Set<Long> parentIdSet = new HashSet<>();
	    
	    for (String s : split) {
	        long ngramAvg = strToVecNgram1D(s, 512, 2);
	        
	        int cost = SimilarWards.maxLength(s.length(), false,maxLen);
	      //  long startTime = System.currentTimeMillis();
	        parentIdSet.add(insertToVec(ngramAvg,cost));
	      //  long endTime = System.currentTimeMillis();
	        
	        

	    }

	    return new ArrayList<>(parentIdSet);
	}
	//vecの存在確認とインサートを実行

	public List<VecDB> selectStr(String input, int tableId) {
	    if (input.length() > 100) throw new IllegalArgumentException("入力文字が長すぎます");

	    String[] strs = input.trim().split("\\s+|　+");
	    List<T> all = new ArrayList<>();
	    for (String str : strs) {
	        List<T> result = calcMatchedStr(str, tableId);
	        all.addAll(result);
	    }

	    // vecParent_id ごとにグループ化して max/avg を集計
	    Map<Long, VecDB> summarized = all.stream()
	        .filter(Objects::nonNull)
	        .filter(v -> v.getVecParent_id() != null)
	        .collect(Collectors.groupingBy(
	            VecDB::getVecParent_id,
	            Collectors.collectingAndThen(
	                Collectors.toList(),
	                list -> {
	                    int sumMatched = list.stream()
	                        .mapToInt(VecDB::getMaxMatched)
	                        .sum();

	                    double sum = list.stream()
	                        .mapToDouble(VecDB::getMaxLineDiff)
	                        .sum();

	                    double avgLineDiff = list.isEmpty() ? 0.0 : sum / strs.length;
	                    return new VecDB(list.get(0).getVecParent_id(), sumMatched / strs.length, avgLineDiff, sum);
	                }
	            )
	        ));

	    List<VecDB> sortedList = summarized.values().stream()
	        .sorted(Comparator
	            .comparingDouble(VecDB::getMaxMatched).reversed()
	            .thenComparing(Comparator.comparingDouble(VecDB::getMatchRatio).reversed())
	        )
	        .collect(Collectors.toList());

	    System.out.println(sortedList);
	    return sortedList;
	}


	protected List<T>  calcMatchedStr(String str,int tableId){
		String split[] = strToArr(str) ;
		//List<BigInteger>  vecId = getWordVecs(split,true);

		int countSplit=split.length;
		
		
		long avgNgrams[]=new long[countSplit];
	    int costs[] =new int[countSplit];
	    for (int i = 0; i < countSplit; i++) {

	        String s = split[i];
	        
	        long avgNgram = strToVecNgram1D(s, 512, 2);
	        

	        avgNgrams[i]=avgNgram;
	        costs[i]=SimilarWards.maxLength(s.length(), false,maxLen);
	        
	    }
	    List<T> result=vec.selectMachedStr( avgNgrams,costs,tableId);

	    
	    
	    return result;
	    
	}


	protected String[] strToArr(String str) {
        String searchTxt=(TextRep.main(str,false));
		
		Integer max= SimilarWards.maxLength(searchTxt.length(), false,maxLen);
		String[] split = SimilarWards.splitStr(searchTxt, max, false,maxLen);
		String[] uniqueSplit = Arrays.stream(split).distinct().toArray(String[]::new);
		return uniqueSplit ;
		
	}
	
	
	
	public static long  strToVecNgram1D(String s, int dim, int ngram) {
		long[] vec = new long[dim];
	    int L = s.length();
	    if (L < ngram) return 0L;

	    for (int i = 0; i < L - ngram + 1; i++) {
	        long h = 2166136261L;
	        for (int j = 0; j < ngram; j++) {
	            h ^= s.charAt(i + j);
	            h *= 16777619L;
	            h &= 0xFFFFFFFFFFFFFFFFL;
	        }

	        int index1 = (int)((h + i * 1315423911L) % dim);
	        int index2 = (int)((h * 31 + i * 314159L) % dim);
	        int index3 = (int)((h * 7 + i * 271828L) % dim);

	        if (index1 < 0) index1 += dim;
	        if (index2 < 0) index2 += dim;
	        if (index3 < 0) index3 += dim;

	        
	        long v = (h % 1000000);  
	        long noise = ((h + i * 31 + L * 17) % 100000); 

	        vec[index1] += v + noise;
	        vec[index2] += (long)((v * 7) / 10) + (long)((noise * 7) / 10);
	        vec[index3] += (long)(v / 2) + (long)(noise / 2);
	    }

	    long sum = 0L;
	    for (long x : vec) sum += x;

	    return sum / dim;
	}






	
}

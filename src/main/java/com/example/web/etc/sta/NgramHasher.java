package com.example.web.etc.sta;

import java.util.Arrays;

public final class NgramHasher {

	 private NgramHasher() {} // インスタンス化禁止
	 
	public static String[] strToArr(String str,int lenNgram) {
		
		
        String searchTxt=(Kakasi.katakanaToHiragana(TextRep.main(str, false)));
		
		Integer max= SimilarWards.maxLength(searchTxt.length(), false,lenNgram);
		String[] split = SimilarWards.splitStr(searchTxt, max, false,lenNgram);
		String[] uniqueSplit = Arrays.stream(split).distinct().toArray(String[]::new);
		return uniqueSplit ;
		
	}
	
	
	
	public static long  strToVecNgram1D(String s, int ngram) {
		int dim=3;
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

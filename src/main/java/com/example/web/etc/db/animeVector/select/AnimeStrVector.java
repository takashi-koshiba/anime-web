package com.example.web.etc.db.animeVector.select;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    protected List<AnimeVecDB> calcMatchedStr(String str, int tableId) {
        String[] split = strToArr(str);

        int countSplit = split.length;
        long[] avgNgrams = new long[countSplit];
        int[] costs = new int[countSplit];

        for (int i = 0; i < countSplit; i++) {
            String s = split[i];
            long avgNgram = strToVecNgram1D(s, 512, 2);
            avgNgrams[i] = avgNgram;
            costs[i] = SimilarWards.maxLength(s.length(), false,maxLen);
        }

        List<AnimeVecDB> result = vec.selectMachedStr(avgNgrams, costs, tableId);
        return result;
    }

    @Override
    public List<VecDB> selectStr(String input, int tableId) {
        if (input.length() > 100)
            throw new IllegalArgumentException("入力文字が長すぎます: ");

       //long startTime = System.currentTimeMillis();

        int countInputLen = 0;
        String[] strs = input.trim().split("\\s+|　+");
        List<AnimeVecDB> all = new ArrayList<>();

        for (String str : strs) {
            List<AnimeVecDB> result = calcMatchedStr(str, tableId);
   
            all.addAll(result);
            countInputLen += str.length();
        }
        
        final int countInputLen_f = countInputLen;

        Map<Long, AnimeVecDB> summarized = all.stream()
            .filter(Objects::nonNull)
            .filter(v -> v.getVecParent_id() != null)
            .collect(Collectors.groupingBy(
                AnimeVecDB::getVecParent_id,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> {
                        int sumMatched = list.stream().mapToInt(AnimeVecDB::getMaxMatched).sum();
                        double sum = list.stream().mapToDouble(AnimeVecDB::getMaxLineDiff).sum();
                        double avgLineDiff = list.isEmpty() ? 0.0 : sum / strs.length;
                        return new AnimeVecDB(list.get(0).getVecParent_id(), sumMatched / strs.length, avgLineDiff, sum, 0, 1, list.get(0).getOriginal(), list.get(0).getTitle());
                    }
                )
            ));

        List<VecDB> sortedList = summarized.values().stream()
            .sorted(Comparator
                .comparingDouble(AnimeVecDB::getMaxMatched).reversed()
                .thenComparing(Comparator.comparingDouble(AnimeVecDB::getMatchRatio).reversed())
            )
            .collect(Collectors.toList());

       
        return sortedList;
    }
}

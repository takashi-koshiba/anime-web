package com.example.web.etc.db.animeVector.select;

import com.example.web.etc.db.vector.VecDB;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AnimeVecDB extends VecDB {
	/*
	private Long vecParent_id;
	private int maxMatched;
	private double maxLineDiff;
	private double matchRatio;
	*/
	private int id;
	private double distance;
	private String original;
	private String title;
	
	
	public AnimeVecDB(Long vecParent_id, int maxMatched, double maxLineDiff, double matchRatio,
            int id, double distance, String original, String title) {
super(vecParent_id, maxMatched, maxLineDiff, matchRatio);

// 追加フィールド
this.id = id;
this.distance = distance;
this.original = original;
this.title = title;

// 念のため親クラスのsetterでも値を上書き
setVecParent_id(vecParent_id);
setMaxMatched(maxMatched);
setMaxLineDiff(maxLineDiff);
setMatchRatio(matchRatio);
}
	public String toString() {
	    return "AnimeVecDB(vecParent_id=" + getVecParent_id() +
	    	       ", maxMatched=" + getMaxMatched() +
	    	       ", maxLineDiff=" + getMaxLineDiff() +
	    	       ", matchRatio=" + getMatchRatio() +
	    	       ", id=" + this.id +
	    	       ", distance=" + this.distance +
	    	       ", original=" + this.original +
	    	       ", title=" + this.title + ")";
	}
}

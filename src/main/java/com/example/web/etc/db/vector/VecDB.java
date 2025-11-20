package com.example.web.etc.db.vector;

import lombok.Data;

@Data
public class VecDB {
	private Long vecParent_id;
	private int maxMatched;
	private double maxLineDiff;
	private double matchRatio;
	
	public VecDB(Long id,int matched,double diff,double matchRatio){
		vecParent_id=id;
		maxMatched=matched;
		maxLineDiff=diff;
		this.matchRatio=matchRatio;
	}
}

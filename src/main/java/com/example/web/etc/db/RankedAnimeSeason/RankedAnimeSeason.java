package com.example.web.etc.db.RankedAnimeSeason;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class RankedAnimeSeason {
	private Integer anime_id;
	private Integer year;
	private Integer season;
	private Integer all_ranking; 
	private String originalName;
	private String foldername;
	private BigDecimal score;
	private String txt;
	private BigInteger ranking;
	

}

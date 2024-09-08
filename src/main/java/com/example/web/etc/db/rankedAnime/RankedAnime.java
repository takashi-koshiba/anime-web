package com.example.web.etc.db.rankedAnime;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RankedAnime {
	private Integer anime_id;
	private String originalName;
	private String foldername;
	private BigDecimal score;
	private String txt;
	private Integer ranking;
	

}

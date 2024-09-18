package com.example.web.etc.db.Animetable;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AnimeSort {
	private Integer id;
	private String originalName;
	private String foldername;
	private String txtChar;
	private Long  charId;
	private Integer sort;
	private BigDecimal score;

}

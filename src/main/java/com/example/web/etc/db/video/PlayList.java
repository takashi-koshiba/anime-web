package com.example.web.etc.db.video;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PlayList {
	private String fname;
	private BigDecimal score;
	private Long nocmframe;
	private String hiduke;
	private Long video_time;
	private String foldername;
	private String title;

}

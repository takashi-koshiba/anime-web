package com.example.web.etc.db.video;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Video {
	private String fname;

	private BigDecimal score;
	private Long nocmframe;
	private String hiduke;
	private String video_time;
    private Integer video_id;
    private Integer row;
}

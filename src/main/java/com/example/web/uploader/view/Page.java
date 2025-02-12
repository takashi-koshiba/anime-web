package com.example.web.uploader.view;

import java.util.List;

import lombok.Data;

@Data
public class Page {
	private String prevUrl;
	private String nextUrl;
	private Integer prevNum;
	private Integer nextNum;
	
	private List<PageNumbers> pageNumbers;
	
	
	
}


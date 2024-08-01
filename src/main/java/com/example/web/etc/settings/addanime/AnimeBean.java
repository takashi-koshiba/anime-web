package com.example.web.etc.settings.addanime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AnimeBean {

	private MultipartFile upload[];

	public AnimeBean(MultipartFile upload[]) {
		this.upload=upload;
	}
	
}
package com.example.web.rest.db.anime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
@RestController

public class SelectAll {
	@Autowired
	AnimeService animeService;
	
	@GetMapping("/anime-web/api/db/select-all/")
	public List<Anime> start()  {
		return animeService.selectAll();
	}
	

}

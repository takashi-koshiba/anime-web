package com.example.web.rest.db.rankedAnime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.rankedAnime.RankedAnime;
import com.example.web.etc.db.rankedAnime.RankedAnimeService;
@RestController

public class SelectRankedAnimeAll {
	@Autowired
	RankedAnimeService RankedAnimeService;
	
	@GetMapping("/anime-web/api/db/rankedAnime/")
	public List<RankedAnime> start()  {

		return RankedAnimeService.selectAll();
		
		
	}
	
	@GetMapping("/anime-web/api/db/rankedAnime/{animeId}")
	
	public RankedAnime start(@PathVariable Integer animeId)  {

		return RankedAnimeService.selectOne(animeId);
		
		
	}
	
}

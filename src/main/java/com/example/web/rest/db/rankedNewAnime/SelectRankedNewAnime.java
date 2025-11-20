package com.example.web.rest.db.rankedNewAnime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.rankedNewAnime.RankedNewAnime;
import com.example.web.etc.db.rankedNewAnime.RankedNewAnimeService;
@RestController

public class SelectRankedNewAnime{
	@Autowired
	RankedNewAnimeService rankedNewAnimeService;
	
	@GetMapping("/anime-web/api/db/rankedNewAnime/")
	public List<RankedNewAnime> start(@RequestParam(required = false) String year, @RequestParam(required = false) String season)  {
	    
		
		if (season != null && year!=null) {
	        return rankedNewAnimeService.selectAll(year, season);
	    } else if(season != null) {
	        return rankedNewAnimeService.selectAll(year);
	    }else {
	    	
	    	return rankedNewAnimeService.selectAll();
	    }
	}
}

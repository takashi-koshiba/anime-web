package com.example.web.rest.db.rankedAnimeSeason;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.RankedAnimeSeason.RankedAnimeSeason;
import com.example.web.etc.db.RankedAnimeSeason.RankedAnimeSeasonService;
@RestController

public class SelectRankedAnimeSeasonAll{
	@Autowired
	RankedAnimeSeasonService RankedAnimeSeasonService;
	
	@GetMapping("/anime-web/api/db/rankedAnimeSeason/")
	public List<RankedAnimeSeason> start(@RequestParam(required = false) String year, @RequestParam(required = false) String season)  {
	    
		
		if (season != null && year!=null) {
	        return RankedAnimeSeasonService.selectAll(year, season);
	    } else if(season != null) {
	        return RankedAnimeSeasonService.selectAll(year);
	    }else {
	    	
	    	return RankedAnimeSeasonService.selectAll();
	    }
	}
}

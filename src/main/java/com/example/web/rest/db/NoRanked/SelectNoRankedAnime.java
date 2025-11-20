package com.example.web.rest.db.NoRanked;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.NoRankedAnime.NoRankedAnime;
import com.example.web.etc.db.NoRankedAnime.NoRankedAnimeService;
@RestController

public class SelectNoRankedAnime{
	@Autowired
	NoRankedAnimeService noRankedAnimeService;
	
	@GetMapping("/anime-web/api/db/noRanked/")
	public List<NoRankedAnime> start(@RequestParam(required = false) String year, @RequestParam(required = false) String season)  {
	    
		
		if (season != null && year!=null) {
			
	        return noRankedAnimeService.selectAll(year, season);
	    } else if(season != null) {
	    	
	        return noRankedAnimeService.selectAll(year);
	    }else {
	    	
	    	return noRankedAnimeService.selectAll();
	    }
	}
}

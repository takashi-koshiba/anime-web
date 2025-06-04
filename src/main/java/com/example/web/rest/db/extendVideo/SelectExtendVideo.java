package com.example.web.rest.db.extendVideo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.extendVideoRank.ExtendVideo;
import com.example.web.etc.db.extendVideoRank.ExtendVideoService;
@RestController

public class SelectExtendVideo{
	@Autowired
	ExtendVideoService extendVideoService;
	
	@GetMapping("/anime-web/api/db/extendVideo/")
	public List<ExtendVideo> start(@RequestParam(required = false) String year, @RequestParam(required = false) String season)  {
	    
		
		if (season != null && year!=null) {

	        return extendVideoService.selectAll(year, season);
	    } else if(season != null) {

	        return extendVideoService.selectAll(year);
	    }else {

	    	return extendVideoService.selectAll();
	    }
	}
}

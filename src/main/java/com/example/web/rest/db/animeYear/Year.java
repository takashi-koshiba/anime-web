package com.example.web.rest.db.animeYear;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.AnimeYear1.AnimeYear;
import com.example.web.etc.db.AnimeYear1.AnimeYearService;
@RestController

public class Year {
	@Autowired
	AnimeYearService AnimeYearService;
	
	@GetMapping("/anime-web/api/db/getYear/")
	public List<AnimeYear> start()  {

		return AnimeYearService.selectAll();
		
		
	}
	
	
}

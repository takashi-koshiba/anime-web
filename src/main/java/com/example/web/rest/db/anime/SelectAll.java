package com.example.web.rest.db.anime;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.Animetable.AnimeSort;
import com.example.web.etc.db.Animetable.prefix;
@RestController

public class SelectAll {
	@Autowired
	AnimeService animeService;
	
	@GetMapping("/anime-web/api/db/select-all/")
	public List<Anime> start()  {
		return animeService.selectAll();
	}

	@GetMapping("/anime-web/api/db/select-allSort/{charId}")
	public List<AnimeSort> start2(@PathVariable Integer charId)  {
		return animeService.selectAllSortByText(charId);
	}
	@GetMapping("/anime-web/api/db/select-allSort/")
	public List<AnimeSort> start4()  {
		return animeService.selectAllSortByText(-1);
	}
	
	@GetMapping("/anime-web/api/db/selectPrefix/")
	public List<prefix> start3()  {
		return animeService.selectPrefixAll();
	}
	


}

package com.example.web.rest.db.animeDistance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.sta.CalcCost;
import com.example.web.etc.sta.Kakasi;
import com.example.web.etc.sta.TextRep;
@RestController

public class AnimeLength {
	@Autowired
	AnimeService animeService;
	
	
	
	@GetMapping("/anime-web/api/db/animeLen/{str}")
	public List<StrDistance> start(@PathVariable("str") String str)  {
		
		String inputText=TextRep.main(Kakasi.main(str));

		List<Anime> animeList=animeService.selectAll();
		List<StrDistance> distanceList=  new ArrayList<>();
		
		for(Integer i=0;i<animeList.size();i++) {
			insertDistList(distanceList,inputText,animeList.get(i));
		}

		distanceList.sort(Comparator.comparingDouble(item -> ((StrDistance) item).getDistance()).reversed());

		return distanceList;
	}

	private List<StrDistance> insertDistList(List<StrDistance> distanceList,String str,Anime anime) {
		StrDistance distance= new StrDistance();
		Double dist;
		dist=getMatchStrCount(str,anime);
		

		distance.setDistance(dist);
		distance.setId(anime.getId());
		distance.setTitle(anime.getFoldername());
		distance.setOriginal(anime.getOriginalName());
		distanceList.add(distance);
		
		return distanceList;
	}


	
	private Double getMatchStrCount(String str,Anime  target) {
		return (double)CalcCost.getMaxCost(str,target.getFoldername());
		
	}

}

package com.example.web.rest.db.animeDistance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	
	
	//@GetMapping("/anime-web/api/db/animeLen/{str}")
	@PostMapping("/anime-web/api/db/animeLen/")
	public List<StrDistance> start(@RequestParam("txt") String str)  {
		String inputText=Kakasi.main(TextRep.main(str ),"-JH -KH");
		
		if(inputText.equals("")) {
			return new ArrayList<>();
		}
		
		//inputの文字数が長い場合は短いアニメ除外
		Integer minLength=(int)Math.ceil((double)inputText.length()/6);
		
		List<Anime> animeList=animeService.selectGt(minLength);
		List<StrDistance> distanceList=  new ArrayList<>();
		
		
		if (animeList.size()==0) {
			return distanceList;
		}

		Integer inputLen=inputText.length();
		Boolean isShort=inputLen<4;
		Integer maxCost=CalcCost.maxCost(inputLen,isShort);
		String[] splitStr=CalcCost.splitStr(inputText,maxCost,isShort);

		
		for(Integer i=0;i<animeList.size();i++) {
			insertDistList(distanceList,inputText,animeList.get(i),maxCost,splitStr,isShort);
		}

		distanceList.sort(Comparator.comparingDouble(item -> ((StrDistance) item).getDistance()).reversed());

		inputText=null;
		animeList=null;
		
		return distanceList;
	}

	private List<StrDistance> insertDistList(List<StrDistance> distanceList,String str,
			Anime anime,Integer maxCost,String[] splitStr,Boolean isShort) {
		
		StrDistance distance= new StrDistance();
		Double dist=getMatchStrCount(str,anime,maxCost,splitStr,isShort);
		

		distance.setDistance(dist);
		distance.setId(anime.getId());
		distance.setTitle(anime.getFoldername());
		distance.setOriginal(anime.getOriginalName());
		distanceList.add(distance);
		
		return distanceList;
	}


	
	private Double getMatchStrCount(String str,Anime  target,Integer maxCost,String[] splitStr,Boolean isShort) {
		
		return (double)CalcCost.getMaxCost(target.getFoldername(),maxCost,splitStr,isShort);
		
	}

}

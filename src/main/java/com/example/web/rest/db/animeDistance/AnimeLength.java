package com.example.web.rest.db.animeDistance;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.animeVector.select.AnimeStrVector;
import com.example.web.etc.db.animeVector.select.AnimeVecDB;
import com.example.web.etc.sta.Kakasi;
import com.example.web.etc.sta.SimilarWards;
import com.example.web.etc.sta.TextRep;
@RestController

public class AnimeLength {
	@Autowired
	AnimeService animeService;
	
	@Autowired
	AnimeStrVector animeStrVector;
	
	//@GetMapping("/anime-web/api/db/animeLen/{txt}")
	@PostMapping("/anime-web/api/db/animeLen/{modeIndex}")
	public List<StrDistance> start(@PathVariable("modeIndex")int modeIndex, @RequestParam("txt") String str, @RequestParam("limit") int limit)  {
		//String inputText=Kakasi.main(TextRep.main(str ,true),"-JH -KH");
		//String inputText=Kakasi.main(TextRep.main(str ,true),"-KH ");
		String inputText=Kakasi.katakanaToHiragana(TextRep.main(str, true));
		
		

		
		List<StrDistance> distances =
			    animeStrVector.selectStr(inputText, modeIndex,limit)
			        .stream()
			        .map(v -> (AnimeVecDB) v)
			         
			        .map(v -> {
			            StrDistance sd = new StrDistance();
			            sd.setId(v.getVecParent_id() != null ? v.getVecParent_id().intValue() : 0);
			            sd.setDistance((double) v.getMaxLineDiff());
			            sd.setOriginal(v.getOriginal());
			            sd.setTitle(v.getTitle());
			            return sd;
			        })
			        .collect(Collectors.toList());
			
			return distances;
		/*
		String inputText=Kakasi.main(TextRep.main(str ,true),"-JH -KH");

		inputText = inputText.replace("　", " ");
		inputText = inputText.replaceAll(" {2,}", " ");
		inputText = inputText.trim();

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
		//スペースで分割
		
		String[] inputs = inputText.split(" ");
		Integer[] inputLen = new Integer[inputs.length];
		Boolean[] isShort = new Boolean[inputs.length];
		Integer[] maxCost = new Integer[inputs.length];
		String[][] splitStr = new String[inputs.length][]; 

		for (int i = 0; i < inputs.length; i++) {
		    inputLen[i] = inputs[i].length();
		    isShort[i] = inputLen[i] < 4;
		    maxCost[i] = SimilarWards.maxLength(inputLen[i], isShort[i]);
		    splitStr[i] =SimilarWards.splitStr(inputs[i], maxCost[i], isShort[i]);
		}
		
		
		

		

		
		for(Integer i=0;i<animeList.size();i++) {
			Double dist=0d;

			
			for(Integer j=0;j<inputs.length;j++) {
				dist+=(getMatchStrCount(str,animeList.get(i),maxCost[j],splitStr[j],isShort[j]))*((double)1/(double)inputs.length);

			}
			dist=dist/(double) inputs.length;
			insertDistList(distanceList,animeList.get(i),dist);
		}

		distanceList.sort(Comparator.comparingDouble(item -> ((StrDistance) item).getDistance()).reversed());

		inputText=null;
		animeList=null;
		
		return distanceList;
		
	}

	private List<StrDistance> insertDistList(List<StrDistance> distanceList,Anime anime,Double dist) {
		
		StrDistance strDistance= new StrDistance();
		
		strDistance.setDistance(dist);
		strDistance.setId(anime.getId());
		strDistance.setTitle(anime.getFoldername());
		strDistance.setOriginal(anime.getOriginalName());
		distanceList.add(strDistance);
		
		return distanceList;
		*/
	}


	
	private Double getMatchStrCount(String str,Anime  target,Integer maxCost,String[] splitStr,Boolean isShort) {
		int maxLen=2;
		return (double)SimilarWards.exec(target.getFoldername(),maxCost,splitStr,isShort,maxLen);
		
	}

}

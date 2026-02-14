package com.example.web.rest.db.animeDistance;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.fulltext_search.prog.selectDB.selectprogHash;
import com.example.web.etc.db.fulltext_search.prog.selectDB.selectprogHashService;
import com.example.web.etc.db.fulltext_search.videoTitle.selectDB.AnimeHash;
import com.example.web.etc.db.fulltext_search.videoTitle.selectDB.AnimeHashService;
import com.example.web.etc.sta.SimilarWards;
@RestController

public class AnimeLength {
	//@Autowired
	//AnimeService animeService;
	
	@Autowired
	selectprogHashService selectProgService;
	
	@Autowired
	AnimeHashService  animeHashService;
	
	
	//@GetMapping("/anime-web/api/db/animeLen/{txt}")
	@PostMapping("/anime-web/api/db/animeLen/{modeIndex}")
	public List<StrDistance> start(@PathVariable("modeIndex")int modeIndex, @RequestParam("txt") String str, @RequestParam("limit") int limit)  {
		//String inputText=Kakasi.main(TextRep.main(str ,true),"-JH -KH");
		//String inputText=Kakasi.main(TextRep.main(str ,true),"-KH ");
		
		if(str.length()>50) {
			 throw new ResponseStatusException(
		                HttpStatus.BAD_REQUEST, 
		                "文字数が多すぎます。"
		        );
			 
		}
		//String inputText=Kakasi.katakanaToHiragana(TextRep.main(str, true));
		
		
		
		//タイトル検索
		if(modeIndex==1) {

			List<StrDistance> distances =
				    //animeStrVector.selectStr(inputText, modeIndex,limit)
				    animeHashService.selectByHash(str)
				        .stream()
				        .map(v -> (AnimeHash) v)
				         
				        .map(v -> {
				            StrDistance sd = new StrDistance();
				            sd.setId(v.getAnimeId());
				            sd.setDistance((double) v.getScore());
				            sd.setOriginal(v.getOriginalName());
				            sd.setTitle(v.getFoldername());
				            return sd;
				        })
				        .collect(Collectors.toList());
				
				return distances;
		}else if(modeIndex==2) {//番組表検索
			List<StrDistance> distances =
				    //animeStrVector.selectStr(inputText, modeIndex,limit)
					selectProgService.selectByHash(str)
				        .stream()
				        .map(v -> (selectprogHash) v)
				         
				        .map(v -> {
				            StrDistance sd = new StrDistance();
				            sd.setId(v.getAnimeId());
				            sd.setDistance((double) v.getScore());
				            sd.setOriginal(v.getOriginalName());
				            sd.setTitle(v.getFoldername());
				            return sd;
				        })
				        .collect(Collectors.toList());
				
				return distances;
		}
		
		throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "パラメータエラー"
        );
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

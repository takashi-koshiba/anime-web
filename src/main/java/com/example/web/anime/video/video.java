package com.example.web.anime.video;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.rankedAnime.RankedAnime;
import com.example.web.etc.db.rankedAnime.RankedAnimeService;
import com.example.web.etc.db.video.Video;
import com.example.web.etc.db.video.VideoService;
import com.example.web.etc.sta.Setting;


@Controller
public class video {
	@Autowired
	RankedAnimeService RankedAnimeService;

	@Autowired
	 AnimeService animeService;
	
	@Autowired
	VideoService videoService;
	
	
	@GetMapping("/anime-web/anime/video/{id}")
	public ModelAndView  start(@PathVariable("id") Integer id) {

		RankedAnime animeInfo =RankedAnimeService.selectOne(id);
		ModelAndView model= new ModelAndView("anime-web/anime/video/index");
		model.addObject("animeInfo",animeInfo);
		
		List<Anime> anime =animeService.selectOne(id);
		model.addObject("anime",anime.getFirst());
		
		
		Integer rows = RankedAnimeService.countRow();
		model.addObject("rows",rows);
		

				
		List<Video> video =videoService.selectOne(id);
		model.addObject("video",video);
		
		
		String thumnailPath=Setting.getUrl()+"content/anime-web/anime/img/"+anime.getFirst().getFoldername()+"/";
		//System.out.print(+video.getFirst().getFname()+"/"+"output_0.jpg");
		model.addObject("thumnailPath",thumnailPath);
		return model;
	}


}

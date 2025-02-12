package com.example.web.anime.video;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.rankedAnime.RankedAnime;
import com.example.web.etc.db.rankedAnime.RankedAnimeService;
import com.example.web.etc.db.video.Video;
import com.example.web.etc.db.video.VideoService;
import com.example.web.index.BeanUser;
import com.example.web.index.GetIP;


@Controller
public class video {
	@Autowired
	RankedAnimeService RankedAnimeService;

	@Autowired
	 AnimeService animeService;
	
	@Autowired
	VideoService videoService;
	
	
	@GetMapping("/anime-web/anime/video/{id}")
	public ModelAndView  start(@PathVariable("id") Integer id,@RequestParam(value = "sort", required = false) Integer sort ,
			HttpServletRequest request) {
		

		
		
		RankedAnime animeInfo =RankedAnimeService.selectOne(id);
		ModelAndView model= new ModelAndView("anime-web/anime/video/index");
		model.addObject("animeInfo",animeInfo);
		
		List<Anime> anime =animeService.selectOne(id);
		model.addObject("anime",anime.getFirst());
		
		
		Integer rows = RankedAnimeService.countRow();
		model.addObject("rows",rows);
		


		Integer sortIndex=sort ==null?0:sort;
		
		ObjSort[]  sortObj=new ObjSort[] {new ObjSort("タイトル",1),new ObjSort("日付",5)};
		
		Info info =new Info(sortIndex,sortObj);
		model.addObject("info",info);
		
	
		List<Video> videoInfo =videoService.selectOne(id,info.getSortId());
		model.addObject("videoInfo",videoInfo);
		
		model.addObject("videoRows",videoInfo.size());
		
		
		String animeThumnailPath="/anime-web/get-file/anime/image/thumbnail/"+anime.getFirst().getId();
		model.addObject("animeThumnailPath",animeThumnailPath);
		
		//String videoThumnailPath="/anime-web/get-file/anime/image/video-thumbnail/"+anime.getFirst().getFoldername()+"/";
		//System.out.print(+video.getFirst().getFname()+"/"+"output_0.jpg");
		//model.addObject("videoThumnailPath",videoThumnailPath);
		
		BeanUser user =GetIP.GetNameAndIp(request);
		model.addObject("user",user);
		//String blob="";
		
		return model;
	}


}

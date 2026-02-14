package com.example.web.anime.video.alias;

import java.util.List;
import java.util.logging.Level;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.db.animeVector.insert.AnimeInsertService;
import com.example.web.etc.db.fulltext_search.videoTitle.insertDB.InsertTitle;
import com.example.web.etc.db.fulltext_search.videoTitle.parent.ParentService;
import com.example.web.etc.sta.Log;
import com.example.web.index.BeanUser;
import com.example.web.index.GetIP;


@Controller
public class alias {
	
	@Autowired
	AnimeService animeService;
	
	
	@Autowired
	ParentService parentService;
	
	@Autowired
	InsertTitle insertTitle;
	
	
	@Autowired
	AnimeInsertService animeInsertService;
	
	@GetMapping("/anime-web/anime/video/video-alias/{id}")
	public ModelAndView  start(@PathVariable("id") Integer id,HttpServletRequest request) {

		BeanUser user =GetIP.GetNameAndIp(request);
		if(!user.isAdmin()) {
			String path="/anime-web/etc/error/admin/index";
			ModelAndView model= new ModelAndView(path);
			return model;
		}
		
		ModelAndView model= new ModelAndView("anime-web/anime/video/alias/index");
		model.addObject("animeId",id);
		
		
		List<Anime> anime =animeService.selectOne(id);
		model.addObject("anime",anime.getFirst());
		
		List<Anime> alias =animeService.selectAliasOne(id);
		model.addObject("aliases",alias);
	
		return model;
	}
	@PostMapping("/anime-web/anime/video/video-alias/")
	public ModelAndView  start2(@ModelAttribute Form models,HttpServletRequest request) {
		
		//model.addObject("animeInfo",animeInfo);
		
		BeanUser user =GetIP.GetNameAndIp(request);
		String path="/anime-web/etc/error/admin/index";
	
		if(user.isAdmin()) {
			path="redirect:/anime-web/anime/video/video-alias/"+models.getId();
		}
		
		insertAlias(models.getTitle(),models.getId());
		

		updateHashes(models.getId());
		//animeInsertService.delAnimeVector(models.getId());
		//ベクトル追加
		//animeInsertService.insertTitle(-1);
		
		ModelAndView model= new ModelAndView(path);
		return model;

	}
	private void insertAlias(List<String> alias,Integer id) {
		animeService.insertAlias(id);
		for(String str :alias) {
			if(!str.equals("")) {
				animeService.insertAlias(id,str );
			}
		}
		
	}
	@Transactional
	private void updateHashes(int animeId) {
		try {
			//ベクトル削除
			parentService.delete(animeId);
			//System.out.println(animeService.countUnhashedAnime());
			//追加
			insertTitle.InsertOne(animeId);
		}catch(DataAccessException e) {
			Log.detail(Level.WARNING, "titleハッシュの更新に失敗しました", e);
			throw e;
		}
		
		
	}
	

}

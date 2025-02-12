package com.example.web.rest.getFile.anime.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.Anime;
import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.sta.FileController;

@RestController
public class Thumbnail extends FileController {
	@Autowired
	AnimeService animeService;
	
	public Thumbnail() {
		super("content/anime-web/upload/img/thumbnail/");
	}
	
	@GetMapping("/anime-web/get-file/anime/image/thumbnail/{animeid}")
   public ResponseEntity<Resource> getFile(@PathVariable Integer animeid) {
		Anime anime = animeService.selectOne(animeid).getFirst();
	   return super.getFile(anime.getOriginalName()+".avif",anime.getOriginalName()+".avif",false);
   }
}

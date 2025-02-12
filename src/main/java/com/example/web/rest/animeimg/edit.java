package com.example.web.rest.animeimg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.Animetable.AnimeService;
import com.example.web.etc.sta.Kakasi;
import com.example.web.etc.sta.RemoveExtension;
import com.example.web.etc.sta.TextRep;
@RestController

public class edit {
	@Autowired
	AnimeService animeService;
	
	@PostMapping("/anime-web/api/edit")
	public editBean start(@RequestPart("filename") String filename)  {
		
		String title=RemoveExtension.main(filename);
		String folder=Kakasi.main(TextRep.main(title),"-JH -KH");
		
		boolean exist=animeService.IsExistItem(title);
		
		editBean result=new editBean(filename,folder,title,exist);
		System.out.println(Kakasi.main(TextRep.main(title),"-JH -KH"));
		return result;
		
		
	}
	
	
}

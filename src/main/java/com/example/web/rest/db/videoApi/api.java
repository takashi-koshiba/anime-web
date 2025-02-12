package com.example.web.rest.db.videoApi;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.etc.db.video.PlayList;
import com.example.web.etc.db.video.VideoService;

@RestController
public class api {
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	VideoService videoService;
	
	@GetMapping("/anime-web/api/db/videoApi/playList/{id}")//@PathVariable String id
	public HttpEntity<byte[]> PlayList(@PathVariable  Integer id,@RequestParam(value = "id", required = false, defaultValue = "0") Integer sort) {
		Integer sortId=sort==null?1:sort;
		Integer column[] = new Integer[] {5,2};

		List<PlayList> playlist=videoService.videoPlayList(id,column[sortId]);
		String fname=playlist.getFirst().getTitle()+".m3u8";
		return GetPlayList(playlist,fname);
	}
	@GetMapping("/anime-web/api/db/videoApi/video/{id}")//@PathVariable String id
	public HttpEntity<byte[]> PlayList(@PathVariable Integer id) {


		List<PlayList> playlist=videoService.animeM3u8(id);
		String fname=playlist.getFirst().getFname()+".m3u8";
		return GetPlayList(playlist,fname);
	}
	private HttpEntity<byte[]> GetPlayList(List<PlayList> playlist,String fname)  {
		
		String data = "#EXTM3U\n";
		for(PlayList item:playlist) {
			data+="#EXTINF:"+item.getVideo_time()+", "+item.getFname()+"\n";
			data+="\\\\MAIN\\video\\"+item.getFoldername()+"\\"+item.getFname()+"\n";
			
		}
	    byte[] b=data.getBytes();
	       
	    HttpHeaders headers = new HttpHeaders();
	    
	    try {
	    	fname = URLEncoder.encode(fname, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			fname="playlist.m3u8";
			e.printStackTrace();
		}
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fname);

		return new HttpEntity<byte[]>( b,headers);
	}
}
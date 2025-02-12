package com.example.web.etc.db.video;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VideoJDBC implements VideoDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
	public List<Video> selectOne(Integer id,Integer sortId) {
		try {
			

			String sql = "select video_id,fname,round(score*100,2) as score,COALESCE(nocmframe,0) as nocmframe,hiduke,video_time from video_info where anime_id =? order by ?";
			List<Map<String, Object>> result = jdbc.queryForList(sql,id,sortId);
        
			List<Video> animeList = new ArrayList<>();
			
			//System.out.print(result.getFirst().get("hiduke").getClass().getSimpleName());
			for(Map<String,Object>map:result) {
		
				Video anime = new Video();	
				anime.setVideo_id((Integer)map.get("video_id"));
				anime.setFname((String)map.get("fname"));
				anime.setScore((BigDecimal) map.get("score"));
				anime.setNocmframe((long)map.get("nocmframe"));
				anime.setVideo_id((Integer)map.get("video_id"));
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					anime.setHiduke(dateFormat.format(map.get("hiduke")));
				}catch(Exception e) {
					
				}

				try {
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH時間 mm分ss秒");
					anime.setVideo_time(timeFormat.format(map.get("video_time")));

				}catch(Exception e) {
					
				}
				
				animeList.add(anime);
			}
			
			return animeList;
    	}catch (Exception e) {
	 		List<Video> animeList = new ArrayList<>();
	 		animeList.add(new Video());
	 		return animeList;
    	}
	}
    
    public List<VideoThumbnailInfo> videoThumbnail(Integer videoId){
    	try {
			

			String sql = "select video_id,anime_id,foldername,originalName,fname from video_info join anime on video_info.anime_id = anime.id  where video_id=?;";
			List<Map<String, Object>> result = jdbc.queryForList(sql, videoId);
        
			List<VideoThumbnailInfo> videoList = new ArrayList<>();
			
			//System.out.print(result.getFirst().get("hiduke").getClass().getSimpleName());
			for(Map<String,Object>map:result) {
		
				VideoThumbnailInfo anime = new VideoThumbnailInfo();	
				anime.setVideo_id((Integer)map.get("video_id"));
				anime.setAnime_id((Integer)map.get("anime_id"));
				anime.setFoldername((String) map.get("foldername"));
				anime.setOriginalName((String)map.get("originalName"));
				anime.setFname((String)map.get("fname"));
				

				
				videoList.add(anime);
			}
			
			return videoList;
    	}catch (Exception e) {
	 		List<VideoThumbnailInfo> videoList = new ArrayList<>();
	 		videoList.add(new VideoThumbnailInfo());
	 		return videoList;
    	}
    }
    private List<PlayList> playList(List<Map<String, Object>> result){
    	try {
    		
			List<PlayList> animeList = new ArrayList<>();
			
			//System.out.print(result.getFirst().get("sec").getClass().getSimpleName());
			for(Map<String,Object>map:result) {
				
				PlayList anime = new PlayList();
				anime.setFoldername((String) map.getOrDefault("foldername", null));
				anime.setFname((String) map.getOrDefault("fname", null));
				anime.setScore((BigDecimal) map.getOrDefault("score", null));
				anime.setNocmframe((Long) map.getOrDefault("nocmframe", null));
				anime.setVideo_time((Long) map.getOrDefault("sec", null));
				anime.setTitle((String)map.getOrDefault("originalName", null));
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					anime.setHiduke(dateFormat.format(map.get("hiduke")));
				}catch(Exception e) {
					
				}

				
				animeList.add(anime);
			}
			
			return animeList;
    	}catch (Exception e) {
	 		List<PlayList> animeList = new ArrayList<>();
	 		animeList.add(new PlayList());
	 		return animeList;
    	}
    }
    
    @Override
	public List<PlayList> videoPlayList(Integer id,Integer sortId) {
			

    	String sql = "select foldername,concat(fname,ext) as fname,round(score*100,2) as score,COALESCE(nocmframe,0) as nocmframe,hiduke,TIME_TO_SEC(video_time) as sec,originalName from video_info\n"
					+ "join anime on anime.id=video_info.anime_id where anime_id =? order by ?";
    	List<Map<String, Object>> result = jdbc.queryForList(sql,id,sortId);
    	return playList(result);
    }
    
    
    
    @Override
	public List<PlayList>animeM3u8(Integer animeId) {
			

    	String sql = "select foldername,concat(fname,ext) as fname,round(score*100,2) as score,COALESCE(nocmframe,0) as nocmframe,hiduke,TIME_TO_SEC(video_time) as sec from video_info\n"
					+ "join anime on anime.id=video_info.anime_id where video_id =?";
    	List<Map<String, Object>> result = jdbc.queryForList(sql,animeId);
        
    	return playList(result);
    }
}

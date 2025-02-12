package com.example.web.etc.db.video;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Transactional
@Service
public class VideoService {
    private final VideoJDBC videoJDBC;

    // コンストラクタインジェクション
 
    public VideoService(VideoJDBC videoJDBC) {
        this.videoJDBC = videoJDBC;
    }

    
    public List<Video> selectOne(Integer id,Integer sortId){
    	return videoJDBC.selectOne(id,sortId);
    }

    public List<PlayList>videoPlayList(Integer id,Integer sortId){
    	return videoJDBC.videoPlayList(id,sortId);
    }
    public List<PlayList>animeM3u8(Integer id){
    	return videoJDBC.animeM3u8(id);
    }
    public List<VideoThumbnailInfo> videoThumbnail(Integer videoId){
    	return videoJDBC.videoThumbnail(videoId);
    }
}

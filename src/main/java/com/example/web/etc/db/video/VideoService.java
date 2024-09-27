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

    
    public List<Video> selectOne(Integer id){
    	return videoJDBC.selectOne(id);
    }


}

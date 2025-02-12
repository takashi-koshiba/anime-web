package com.example.web.etc.db.video;

import java.util.List;

public interface VideoDao {
	public List<Video> selectOne(Integer id,Integer sortId);
	public List<PlayList>videoPlayList(Integer id,Integer sortId);
	public List<PlayList> animeM3u8(Integer animeId);
	public List<VideoThumbnailInfo> videoThumbnail(Integer videoId);
}

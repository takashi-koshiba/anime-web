package com.example.web.etc.db.video;

import java.util.List;

public interface VideoDao {
	public List<Video> selectOne(Integer id);
	
}

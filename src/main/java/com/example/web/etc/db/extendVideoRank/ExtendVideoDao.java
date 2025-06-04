package com.example.web.etc.db.extendVideoRank;

import java.util.List;

public interface ExtendVideoDao {
	//public int insert(Anime anime);
	//public int countRow(String text);
	public List<ExtendVideo> selectAll(Integer year,Integer season);
	public List<ExtendVideo> selectAll(Integer year);
	public List<ExtendVideo> selectAll();
}

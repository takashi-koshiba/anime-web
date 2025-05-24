package com.example.web.etc.db.NoRankedAnime;

import java.util.List;

public interface NoRankedAnimeDao {
	//public int insert(Anime anime);
	//public int countRow(String text);
	public List<NoRankedAnime> selectAll(Integer year,Integer season);
	public List<NoRankedAnime> selectAll(Integer year);
	public List<NoRankedAnime> selectAll();
}
